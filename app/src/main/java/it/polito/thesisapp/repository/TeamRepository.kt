package it.polito.thesisapp.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.model.TeamMember
import it.polito.thesisapp.utils.FirestoreConstants
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TeamRepository {
    /**
     * Creates a Flow that emits Team objects based on Firestore updates.
     * The flow handles team data, members, and tasks with their assigned members.
     * @param ref DocumentReference pointing to the team document
     * @return Flow that emits Team objects or null if team doesn't exist
     */
    fun getTeamFlow(ref: DocumentReference): Flow<Team?> = callbackFlow {
        val teamSubscription = ref.addSnapshotListener { snapshot, error ->
            handleTeamSnapshot(snapshot, error, ref)
        }
        awaitClose { teamSubscription.remove() }
    }

    /**
     * Handles team document updates from Firestore.
     * Creates Team object and sets up members listener if team exists.
     * @param snapshot Team document snapshot
     * @param error Potential error from Firestore
     * @param teamRef Reference to team document
     */
    private fun ProducerScope<Team?>.handleTeamSnapshot(
        snapshot: DocumentSnapshot?,
        error: Exception?,
        teamRef: DocumentReference
    ) {
        if (error != null) {
            close(error)
            return
        }

        if (snapshot == null || !snapshot.exists()) {
            trySend(null)
            return
        }

        val team = Team.fromFirestore(
            id = snapshot.id,
            data = snapshot.data ?: emptyMap()
        )

        setupMembersListener(teamRef, team)
    }

    /**
     * Sets up real-time listener for team members subcollection.
     * Triggers member snapshot handler when changes occur.
     * @param teamRef Reference to team document
     * @param team Current team object
     */
    private fun ProducerScope<Team?>.setupMembersListener(
        teamRef: DocumentReference,
        team: Team
    ) {
        teamRef.collection(FirestoreConstants.FirestoreCollections.TEAM_MEMBERS)
            .addSnapshotListener { membersSnapshot, membersError ->
                handleMembersSnapshot(membersSnapshot, membersError, teamRef, team)
            }
    }

    /**
     * Processes member updates from Firestore.
     * Creates TeamMember objects and sets up tasks listener.
     * @param membersSnapshot Query snapshot of members collection
     * @param error Potential error from Firestore
     * @param teamRef Reference to team document
     * @param team Current team object
     */
    private fun ProducerScope<Team?>.handleMembersSnapshot(
        membersSnapshot: QuerySnapshot?,
        error: Exception?,
        teamRef: DocumentReference,
        team: Team
    ) {
        if (error != null) {
            close(error)
            return
        }

        val members = membersSnapshot?.documents?.mapNotNull { doc ->
            TeamMember(
                role = doc.getString(FirestoreConstants.FirestoreFields.TeamMember.ROLE) ?: "",
                profileRef = doc.get(FirestoreConstants.FirestoreFields.TeamMember.PROFILE_REF) as? DocumentReference
            )
        } ?: emptyList()

        setupTasksListener(teamRef, team, members)
    }

    /**
     * Sets up real-time listener for team tasks subcollection.
     * Triggers task snapshot handler when changes occur.
     * @param teamRef Reference to team document
     * @param team Current team object
     * @param members List of team members
     */
    private fun ProducerScope<Team?>.setupTasksListener(
        teamRef: DocumentReference,
        team: Team,
        members: List<TeamMember>
    ) {
        teamRef.collection(FirestoreConstants.FirestoreCollections.TEAM_TASKS)
            .addSnapshotListener { tasksSnapshot, tasksError ->
                handleTasksSnapshot(tasksSnapshot, tasksError, team, members)
            }
    }

    /**
     * Handles task updates from Firestore.
     * Creates Task objects and sets up listeners for assigned members.
     * Emits updated team state with current tasks.
     *
     * @param tasksSnapshot Query snapshot of tasks collection
     * @param error Potential error from Firestore
     * @param team Current team object
     * @param members List of team members
     */
    private fun ProducerScope<Team?>.handleTasksSnapshot(
        tasksSnapshot: QuerySnapshot?,
        error: Exception?,
        team: Team,
        members: List<TeamMember>
    ) {
        if (error != null) {
            close(error)
            return
        }

        // Initialize tasks map to maintain state across updates
        val tasksMap = mutableMapOf<String, Task>()

        if (tasksSnapshot?.documents.isNullOrEmpty()) {
            // If there are no tasks, emit team with empty task list
            trySend(team.copy(members = members, tasks = emptyList()))
            return
        }

        tasksSnapshot?.documents?.forEach { taskDoc ->
            val task = Task.fromFirestore(
                id = taskDoc.id,
                data = taskDoc.data ?: emptyMap()
            )

            // Set up snapshot listener for assigned members
            taskDoc.reference.collection(FirestoreConstants.FirestoreCollections.TASK_ASSIGNED_MEMBERS)
                .addSnapshotListener { assignedMembersSnapshot, assignedMembersError ->
                    if (assignedMembersError != null) {
                        close(assignedMembersError)
                        return@addSnapshotListener
                    }

                    val assignedMembers = assignedMembersSnapshot?.documents
                        ?.mapNotNull { it.get(FirestoreConstants.FirestoreFields.AssignedMember.MEMBER_REF) as? DocumentReference }
                        ?: emptyList()

                    tasksMap[task.id] = task.copy(assignedMembers = assignedMembers)

                    // Emit updated team with current tasks
                    trySend(
                        team.copy(
                            members = members,
                            tasks = tasksMap.values.toList()
                        )
                    )
                }
        }
    }

    /**
     * Fetches assigned members for a specific task.
     * Updates task with assigned members and emits updated team.
     * @param taskRef Reference to task document
     * @param task Current task object
     * @param tasks Mutable list of all tasks
     * @param team Current team object
     * @param members List of team members
     */
    private fun ProducerScope<Team?>.fetchAssignedMembers(
        taskRef: DocumentReference,
        task: Task,
        tasks: MutableList<Task>,
        team: Team,
        members: List<TeamMember>
    ) {
        taskRef.collection(FirestoreConstants.FirestoreCollections.TASK_ASSIGNED_MEMBERS)
            .addSnapshotListener { assignedMembersSnapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val assignedMembers = assignedMembersSnapshot?.documents
                    ?.mapNotNull { it.get(FirestoreConstants.FirestoreFields.AssignedMember.MEMBER_REF) as? DocumentReference }
                    ?: emptyList()

                val updatedTask = task.copy(assignedMembers = assignedMembers)
                val taskIndex = tasks.indexOfFirst { it.id == task.id }

                if (taskIndex != -1) {
                    tasks[taskIndex] = updatedTask
                } else {
                    tasks.add(updatedTask)
                }

                trySend(team.copy(members = members, tasks = tasks))
            }
    }
}