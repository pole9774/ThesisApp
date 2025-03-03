package it.polito.thesisapp.model

enum class TaskStatus(val displayName: String, val color: Long) {
    TODO("To Do", 0xFF808080), // Gray
    IN_PROGRESS("In Progress", 0xFF3498DB), // Blue
    LATE("Late", 0xFFE74C3C), // Red
    DONE("Done", 0xFF2ECC71); // Green

    companion object {
        fun fromString(value: String?): TaskStatus {
            return entries.find { it.name == value } ?: TODO
        }
    }
}