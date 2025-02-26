package it.polito.thesisapp.model

import com.google.firebase.firestore.DocumentReference
import it.polito.thesisapp.utils.FirestoreConstants

/**
 * Data class representing a task.
 *
 * @property id The unique identifier of the task.
 * @property name The name of the task.
 * @property description A brief description of the task.
 * @property assignedMembers A list of DocumentReferences to the members assigned to the task.
 */
data class Task(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val assignedMembers: List<DocumentReference> = emptyList()
) {
    companion object {
        /**
         * Creates a Task object from Firestore data.
         *
         * @param id The unique identifier of the task.
         * @param data A map containing the task data from Firestore.
         * @return A Task object populated with the provided data.
         */
        fun fromFirestore(id: String, data: Map<String, Any>): Task {
            return Task(
                id = id,
                name = data[FirestoreConstants.FirestoreFields.Task.NAME] as? String ?: "",
                description = data[FirestoreConstants.FirestoreFields.Task.DESCRIPTION] as? String
                    ?: "",
                assignedMembers = emptyList()
            )
        }
    }
}
