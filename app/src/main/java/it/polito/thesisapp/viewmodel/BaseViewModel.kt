package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    protected val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    protected fun setError(message: String?) {
        _error.value = message
    }

    protected fun validateNotBlank(value: String, fieldName: String): Boolean {
        return if (value.isBlank()) {
            setError("$fieldName cannot be blank")
            false
        } else {
            true
        }
    }
}