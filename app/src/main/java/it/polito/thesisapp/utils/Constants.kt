package it.polito.thesisapp.utils

object Constants {
    /**
     * Object containing constants for Firestore document field names.
     * This helps maintain consistency and avoid string literals in repository classes.
     */
    object FirestoreFields {
        /**
         * Fields specific to Profile documents
         */
        object Profile {
            const val FIRST_NAME = "firstName"
            const val LAST_NAME = "lastName"
            const val BIRTH_DATE = "birthDate"
            const val TEAMS = "teams"
        }

        object Team {
            const val NAME = "name"
            const val TEAM_ID = "teamId"
        }

        /**
         * Fields specific to TeamMember documents
         */
        object TeamMember {
            const val ROLE = "role"
            const val PROFILE_REF = "profileRef"
        }

        /**
         * Fields specific to Task documents
         */
        object Task {
            const val NAME = "name"
            const val DESCRIPTION = "description"
            const val TASK_ID = "taskId"
        }

        /**
         * Fields specific to AssignedMember documents
         */
        object AssignedMember {
            const val MEMBER_REF = "memberRef"
        }
    }

    object FirestoreValues {
        object TeamMemberRole {
            const val ADMIN = "ADMIN"
        }
    }

    /**
     * Object containing constants for Firestore collection names.
     * This helps maintain consistency and avoid string literals in repository classes.
     */
    object FirestoreCollections {
        /**
         * Constant for the profiles collection.
         */
        const val PROFILES = "profiles"

        /**
         * Constant for the teams collection.
         */
        const val TEAMS = "teams"

        /**
         * Constant for the members subcollection within a team.
         */
        const val TEAM_MEMBERS = "members"

        /**
         * Constant for the tasks subcollection within a team.
         */
        const val TEAM_TASKS = "tasks"

        /**
         * Constant for the assigned members subcollection within a task.
         */
        const val TASK_ASSIGNED_MEMBERS = "assignedMembers"
    }

    /**
     * Object that contains a constant for the user ID.
     */
    object User {
        /**
         * Constant for the user ID.
         */
        const val USER_ID = "1kCdRXoPkwmsNI5QRKn9"
    }

    object Tags {
        const val TEAM_NAME = "teamName"
        const val TASK_NAME = "taskName"
        const val TASK_DESCRIPTION = "taskDescription"
    }
}