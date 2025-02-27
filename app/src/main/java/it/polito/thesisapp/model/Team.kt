package it.polito.thesisapp.model

import it.polito.thesisapp.utils.Constants

/**
 * Data class representing a team.
 *
 * @property id The unique identifier of the team.
 * @property name The name of the team.
 * @property members A list of TeamMember objects representing the members of the team.
 * @property tasks A list of Task objects representing the tasks assigned to the team.
 */
data class Team(
    val id: String = "",
    val name: String = "",
    val members: List<TeamMember> = emptyList(),
    val tasks: List<Task> = emptyList()
) {
    companion object {
        /**
         * Creates a Team object from Firestore data.
         *
         * @param id The unique identifier of the team.
         * @param data A map containing the team data from Firestore.
         * @return A Team object populated with the provided data.
         */
        fun fromFirestore(id: String, data: Map<String, Any>): Team {
            return Team(
                id = id,
                name = data[Constants.FirestoreFields.Team.NAME] as? String ?: "",
                members = emptyList(),
                tasks = emptyList()
            )
        }
    }
}
