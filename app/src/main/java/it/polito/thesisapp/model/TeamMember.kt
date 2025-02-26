package it.polito.thesisapp.model

import com.google.firebase.firestore.DocumentReference

/**
 * Data class representing a team member.
 *
 * @property role The role of the team member within the team.
 * @property profileRef A reference to the profile document of the team member in Firestore.
 */
data class TeamMember(
    val role: String = "",
    val profileRef: DocumentReference? = null
)
