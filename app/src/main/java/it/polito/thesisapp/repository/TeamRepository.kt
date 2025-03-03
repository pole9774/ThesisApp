package it.polito.thesisapp.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.model.TeamMember
import it.polito.thesisapp.utils.Constants
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository class for managing team data.
 */
class TeamRepository {
    private val db = FirebaseFirestore.getInstance()

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
        teamRef.collection(Constants.FirestoreCollections.TEAM_MEMBERS)
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
                role = doc.getString(Constants.FirestoreFields.TeamMember.ROLE) ?: "",
                profileRef = doc.get(Constants.FirestoreFields.TeamMember.PROFILE_REF) as? DocumentReference
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
        teamRef.collection(Constants.FirestoreCollections.TEAM_TASKS)
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
            taskDoc.reference.collection(Constants.FirestoreCollections.TASK_ASSIGNED_MEMBERS)
                .addSnapshotListener { assignedMembersSnapshot, assignedMembersError ->
                    if (assignedMembersError != null) {
                        close(assignedMembersError)
                        return@addSnapshotListener
                    }

                    val assignedMembers = assignedMembersSnapshot?.documents
                        ?.mapNotNull { it.get(Constants.FirestoreFields.AssignedMember.MEMBER_REF) as? DocumentReference }
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
     * Creates a new team in Firestore with the given name, description, and members.
     * Adds the current user as an admin and updates profiles with the team reference.
     *
     * @param teamName Name of the team
     * @param teamDescription Description of the team
     * @param memberIds Set of user IDs to be added to the team
     * @return DocumentReference of the created team
     */
    suspend fun createTeam(
        teamName: String,
        teamDescription: String = "",
        memberIds: Set<String> = setOf(Constants.User.USER_ID)
    ): DocumentReference {
        val teamRef = db.collection(Constants.FirestoreCollections.TEAMS)
            .add(
                mapOf(
                    Constants.FirestoreFields.Team.NAME to teamName,
                    Constants.FirestoreFields.Team.DESCRIPTION to teamDescription
                )
            ).await()

        // Add the current user as admin
        teamRef.collection(Constants.FirestoreCollections.TEAM_MEMBERS)
            .add(
                mapOf(
                    Constants.FirestoreFields.TeamMember.ROLE to Constants.FirestoreValues.TeamMemberRole.ADMIN,
                    Constants.FirestoreFields.TeamMember.PROFILE_REF to db.collection(
                        Constants.FirestoreCollections.PROFILES
                    ).document(Constants.User.USER_ID)
                )
            ).await()

        // Add other selected members
        memberIds.filter { it != Constants.User.USER_ID }.forEach { memberId ->
            teamRef.collection(Constants.FirestoreCollections.TEAM_MEMBERS)
                .add(
                    mapOf(
                        Constants.FirestoreFields.TeamMember.ROLE to Constants.FirestoreValues.TeamMemberRole.MEMBER,
                        Constants.FirestoreFields.TeamMember.PROFILE_REF to db.collection(
                            Constants.FirestoreCollections.PROFILES
                        ).document(memberId)
                    )
                ).await()
        }

        // Update all selected profiles with the team reference
        memberIds.forEach { memberId ->
            db.collection(Constants.FirestoreCollections.PROFILES)
                .document(memberId)
                .update(
                    Constants.FirestoreFields.Profile.TEAMS,
                    FieldValue.arrayUnion(teamRef)
                ).await()
        }

        return teamRef
    }

    /**
     * Creates a new task in Firestore for the specified team.
     *
     * @param teamId ID of the team
     * @param taskName Name of the task
     * @param taskDescription Description of the task
     */
    suspend fun createTask(teamId: String, taskName: String, taskDescription: String) {
        val teamRef = db.collection(Constants.FirestoreCollections.TEAMS).document(teamId)

        teamRef.collection(Constants.FirestoreCollections.TEAM_TASKS)
            .add(
                mapOf(
                    Constants.FirestoreFields.Task.NAME to taskName,
                    Constants.FirestoreFields.Task.DESCRIPTION to taskDescription,
                    Constants.FirestoreFields.Task.CREATION_DATE to Timestamp.now(),
                    Constants.FirestoreFields.Task.STATUS to TaskStatus.TODO.name
                )
            ).await()
    }

    suspend fun updateTaskStatus(teamId: String, taskId: String, status: TaskStatus) {
        db.collection(Constants.FirestoreCollections.TEAMS)
            .document(teamId)
            .collection(Constants.FirestoreCollections.TEAM_TASKS)
            .document(taskId)
            .update(Constants.FirestoreFields.Task.STATUS, status.name)
            .await()
    }
}