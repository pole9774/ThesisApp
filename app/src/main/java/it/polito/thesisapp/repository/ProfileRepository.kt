package it.polito.thesisapp.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository class for managing user profiles.
 */
class ProfileRepository {
    private val db = Firebase.firestore
    private val profilesCollection = db.collection(Constants.FirestoreCollections.PROFILES)

    /**
     * Retrieves a flow of Profile objects for a given user ID.
     *
     * @param userId The unique identifier of the user.
     * @return A Flow emitting Profile objects or null if the profile does not exist.
     */
    fun getProfileFlow(userId: String): Flow<Profile?> = callbackFlow {
        val subscription = profilesCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val profile = snapshot.toObject(Profile::class.java)
                    trySend(profile)
                } else {
                    trySend(null)
                }
            }
        awaitClose { subscription.remove() }
    }

    /**
     * Retrieves a flow of all Profile objects.
     *
     * @return A Flow emitting a list of Profile objects.
     */
    fun getAllProfilesFlow(): Flow<List<Profile>> = callbackFlow {
        val subscription = profilesCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val profiles = snapshot.documents.mapNotNull {
                        it.toObject(Profile::class.java)?.copy(id = it.id)
                    }
                    trySend(profiles)
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose { subscription.remove() }
    }
}