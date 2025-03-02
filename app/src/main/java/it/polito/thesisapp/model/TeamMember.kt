package it.polito.thesisapp.model

import com.google.firebase.firestore.DocumentReference

/**
 * Data class representing a team member.
 *
 * This class models a user's participation in a team, including their role.
 * It is stored as a document in the "members" subcollection of a team document.
 *
 * @property role The role of the team member within the team (e.g., "ADMIN" or "MEMBER")
 * @property profileRef A reference to the profile document of the team member in Firestore
 */
data class TeamMember(
    val role: String = "",
    val profileRef: DocumentReference? = null
)
