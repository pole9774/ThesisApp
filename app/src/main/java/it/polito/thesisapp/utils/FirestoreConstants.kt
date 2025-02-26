package it.polito.thesisapp.utils

object FirestoreConstants {
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
        }

        /**
         * Fields specific to AssignedMember documents
         */
        object AssignedMember {
            const val MEMBER_REF = "memberRef"
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
    object UserID {
        /**
         * Constant for the user ID.
         */
        const val USERID = "1kCdRXoPkwmsNI5QRKn9"
    }
}