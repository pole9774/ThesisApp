package it.polito.thesisapp.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import it.polito.thesisapp.model.ListItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ListRepository {
    private val db = Firebase.firestore
    private val itemsCollection = db.collection("items")

    fun getItemsFlow(): Flow<List<ListItem>> = callbackFlow {
        val subscription = itemsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val items = snapshot.documents.map { doc ->
                        ListItem(
                            id = doc.id,
                            title = doc.getString("title") ?: ""
                        )
                    }
                    trySend(items)
                }
            }
        awaitClose { subscription.remove() }
    }
}