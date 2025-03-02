package it.polito.thesisapp.model

import it.polito.thesisapp.utils.Constants

/**
 * Data class representing a team in the application.
 *
 * This class models a group that can contain multiple members and tasks.
 * It is synchronized with a corresponding document in the Firestore "teams" collection.
 *
 * @property id Unique identifier for this team
 * @property name Display name of the team
 * @property description Longer text describing the team's purpose
 * @property members List of TeamMember objects representing the users in this team
 * @property tasks List of Task objects assigned to this team
 */
data class Team(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val members: List<TeamMember> = emptyList(),
    val tasks: List<Task> = emptyList()
) {
    companion object {
        /**
         * Creates a Team object from Firestore document data.
         *
         * @param id The document ID from Firestore
         * @param data The document data map from Firestore
         * @return A new Team instance populated with the Firestore data
         */
        fun fromFirestore(id: String, data: Map<String, Any>): Team {
            return Team(
                id = id,
                name = data[Constants.FirestoreFields.Team.NAME] as? String ?: "",
                description = data[Constants.FirestoreFields.Team.DESCRIPTION] as? String ?: "",
                members = emptyList(),
                tasks = emptyList()
            )
        }
    }
}