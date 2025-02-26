package it.polito.thesisapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

/**
 * Data class representing a user profile.
 *
 * @property id The unique identifier of the profile.
 * @property firstName The first name of the user.
 * @property lastName The last name of the user.
 * @property birthDate The birth date of the user as a Firebase Timestamp.
 * @property teams A list of DocumentReferences to the teams the user is part of.
 */
data class Profile(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: Timestamp = Timestamp.now(),
    val teams: List<DocumentReference> = emptyList()
)
