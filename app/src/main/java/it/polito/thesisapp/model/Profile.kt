package it.polito.thesisapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

/**
 * Data class representing a user profile in the application.
 *
 * This class models personal information about a user and their team associations.
 * It is synchronized with a corresponding document in the Firestore "profiles" collection.
 *
 * @property id Unique identifier for this profile, mapped from Firestore document ID
 * @property firstName User's first name
 * @property lastName User's last name
 * @property birthDate User's date of birth stored as a Firebase Timestamp
 * @property teams List of references to teams the user belongs to
 */
data class Profile(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: Timestamp = Timestamp.now(),
    val teams: List<DocumentReference> = emptyList()
)
