package it.polito.thesisapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * BaseViewModel is an abstract class that extends ViewModel and provides common functionality
 * for handling error messages and validating input fields.
 */
abstract class BaseViewModel : ViewModel() {
    /**
     * MutableStateFlow to hold error messages.
     */
    private val _error = MutableStateFlow<String?>(null)

    /**
     * StateFlow to expose error messages as an immutable flow.
     */
    val error = _error

    /**
     * Sets the error message.
     *
     * @param message The error message to set.
     */
    protected fun setError(message: String?) {
        _error.value = message
    }

    /**
     * Validates that a given value is not blank.
     *
     * @param value The value to validate.
     * @param fieldName The name of the field being validated.
     * @return True if the value is not blank, false otherwise.
     */
    protected fun validateNotBlank(value: String, fieldName: String): Boolean {
        return if (value.isBlank()) {
            setError("$fieldName cannot be blank")
            false
        } else {
            true
        }
    }
}