package it.polito.thesisapp.model

/**
 * Enum class representing the status of a task.
 *
 * @property displayName The display name of the task status.
 * @property color The color associated with the task status.
 */
enum class TaskStatus(val displayName: String, val color: Long) {
    TODO("To Do", 0xFF808080), // Gray
    IN_PROGRESS("In Progress", 0xFF3498DB), // Blue
    LATE("Late", 0xFFE74C3C), // Red
    DONE("Done", 0xFF2ECC71); // Green

    companion object {
        /**
         * Returns the TaskStatus corresponding to the given string value.
         *
         * @param value The string value representing the task status.
         * @return The corresponding TaskStatus, or TODO if the value is null or does not match any status.
         */
        fun fromString(value: String?): TaskStatus {
            return entries.find { it.name == value } ?: TODO
        }
    }
}