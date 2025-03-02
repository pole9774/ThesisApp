package it.polito.thesisapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import it.polito.thesisapp.utils.Constants

/**
 * Data class representing a task within a team.
 *
 * This class models a work item that can be assigned to team members.
 * It is stored as a document in the "tasks" subcollection of a team document.
 *
 * @property id Unique identifier for this task
 * @property name Display name of the task
 * @property description Detailed description of what the task involves
 * @property creationDate Timestamp when the task was created
 * @property assignedMembers List of references to profile documents of members assigned to this task
 */
data class Task(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val creationDate: Timestamp = Timestamp.now(),
    val assignedMembers: List<DocumentReference> = emptyList()
) {
    companion object {
        /**
         * Creates a Task object from Firestore document data.
         *
         * @param id The document ID from Firestore
         * @param data The document data map from Firestore
         * @return A new Task instance populated with the Firestore data
         */
        fun fromFirestore(id: String, data: Map<String, Any>): Task {
            return Task(
                id = id,
                name = data[Constants.FirestoreFields.Task.NAME] as? String ?: "",
                description = data[Constants.FirestoreFields.Task.DESCRIPTION] as? String ?: "",
                creationDate = data[Constants.FirestoreFields.Task.CREATION_DATE] as? Timestamp
                    ?: Timestamp.now(),
                assignedMembers = emptyList()
            )
        }
    }
}
