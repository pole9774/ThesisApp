package it.polito.thesisapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.polito.thesisapp.model.ListItem
import it.polito.thesisapp.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository = ListRepository()
    private val _items = MutableStateFlow<List<ListItem>>(emptyList())
    val items = _items.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        subscribeToItems()
    }

    private fun subscribeToItems() {
        viewModelScope.launch {
            try {
                repository.getItemsFlow().collect { newItems ->
                    _items.value = newItems
                    isLoading = false
                }
            } catch (e: Exception) {
                error = e.message
                isLoading = false
            }
        }
    }
}