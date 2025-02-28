package it.polito.thesisapp.utils

object Constants {
    /**
     * Object containing Firestore collection names
     */
    object FirestoreCollections {
        const val PROFILES = "profiles"
        const val TEAMS = "teams"
        const val TEAM_MEMBERS = "members"
        const val TEAM_TASKS = "tasks"
        const val TASK_ASSIGNED_MEMBERS = "assignedMembers"
    }

    /**
     * Object containing constants for Firestore document field names.
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

        /**
         * Fields specific to Team documents
         */
        object Team {
            const val TEAM_ID = "teamId"
            const val NAME = "name"
            const val MEMBERS = "members"
            const val TASKS = "tasks"
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
            const val ASSIGNED_MEMBERS = "assignedMembers"
        }

        /**
         * Fields specific to AssignedMember documents
         */
        object AssignedMember {
            const val MEMBER_REF = "memberRef"
        }
    }

    /**
     * Object containing values used in Firestore documents
     */
    object FirestoreValues {
        object TeamMemberRole {
            const val ADMIN = "ADMIN"
            const val MEMBER = "MEMBER"
        }
    }

    /**
     * Object containing navigation-related constants
     */
    object Navigation {
        // Base routes
        private const val HOME_BASE = "home"
        private const val TEAM_BASE = "team"
        private const val CREATE_TEAM_BASE = "create_team"
        private const val CREATE_TASK_BASE = "create_task"

        // Parameters
        object Params {
            const val TEAM_ID = "teamId"
        }

        // Full route patterns with parameter placeholders
        object Routes {
            const val HOME = "${HOME_BASE}/"
            const val TEAM = "${TEAM_BASE}/{${Params.TEAM_ID}}"
            const val CREATE_TEAM = "${CREATE_TEAM_BASE}/"
            const val CREATE_TASK = "${CREATE_TASK_BASE}/{${Params.TEAM_ID}}"
        }

        // Tags for saved state
        object Tags {
            const val TEAM_NAME = "teamName"
            const val TASK_NAME = "taskName"
            const val TASK_DESCRIPTION = "taskDescription"
        }
    }

    /**
     * Object containing user-related constants
     */
    object User {
        const val USER_ID = "1kCdRXoPkwmsNI5QRKn9"
    }
}