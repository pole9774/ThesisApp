package it.polito.thesisapp.navigation

import it.polito.thesisapp.R
import it.polito.thesisapp.utils.Constants

/**
 * Sealed class representing different screens in the application.
 *
 * @property route The route string associated with the screen.
 * @property icon The resource ID of the icon representing the screen.
 * @property label The label of the screen.
 */
sealed class Screen(val route: String, val icon: Int, val label: String) {
    companion object {
        /**
         * Builds the route for the home screen.
         *
         * @return The route string for the home screen.
         */
        fun buildHomeRoute() = Constants.Navigation.Routes.HOME

        /**
         * Builds the route for a team screen with the specified team ID.
         *
         * @param teamId The ID of the team.
         * @return The route string for the team screen.
         */
        fun buildTeamRoute(teamId: String) = Constants.Navigation.Routes.TEAM.replace(
            "{${Constants.Navigation.Params.TEAM_ID}}",
            teamId
        )

        /**
         * Builds the route for the create team screen.
         *
         * @return The route string for the create team screen.
         */
        fun buildCreateTeamRoute() = Constants.Navigation.Routes.CREATE_TEAM

        /**
         * Builds the route for the create task screen with the specified team ID.
         *
         * @param teamId The ID of the team.
         * @return The route string for the create task screen.
         */
        fun buildCreateTaskRoute(teamId: String): String =
            Constants.Navigation.Routes.CREATE_TASK.replace(
                "{${Constants.Navigation.Params.TEAM_ID}}",
                teamId
            )

        /**
         * Builds the route for the profile screen.
         *
         * @return The route string for the profile screen.
         */
        fun buildProfileRoute() = Constants.Navigation.Routes.PROFILE

        fun buildTaskRoute(teamId: String, taskId: String) =
            "${Constants.Navigation.Routes.TEAM_TASK_BASE}/$teamId/$taskId"

        fun isTaskRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.TEAM_TASK_BASE) == true
        }

        /**
         * Checks if a route is the home route.
         *
         * @param route The route to check.
         * @return True if the route is the home route, false otherwise.
         */
        fun isHomeRoute(route: String?): Boolean {
            return route == Constants.Navigation.Routes.HOME
        }

        /**
         * Checks if a route is a team route.
         *
         * @param route The route to check.
         * @return True if the route is a team route, false otherwise.
         */
        fun isTeamRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.TEAM.substringBefore("{")) == true
        }

        /**
         * Checks if a route is the create team route.
         *
         * @param route The route to check.
         * @return True if the route is the create team route, false otherwise.
         */
        fun isCreateTeamRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.CREATE_TEAM) == true
        }

        /**
         * Checks if a route is a create task route.
         *
         * @param route The route to check.
         * @return True if the route is a create task route, false otherwise.
         */
        fun isCreateTaskRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.CREATE_TASK.substringBefore("{")) == true
        }

        /**
         * Checks if a route is the profile route.
         *
         * @param route The route to check.
         * @return True if the route is the profile route, false otherwise.
         */
        fun isProfileRoute(route: String?): Boolean {
            return route == Constants.Navigation.Routes.PROFILE
        }

        /**
         * List of screens to be displayed in the bottom navigation bar.
         */
        val screensBottomBar = listOf(Home, Profile)
    }

    /**
     * Object representing the Home screen.
     */
    data object Home : Screen(Constants.Navigation.Routes.HOME, R.drawable.ic_home, "Home")

    /**
     * Object representing the Profile screen.
     */
    data object Profile :
        Screen(Constants.Navigation.Routes.PROFILE, R.drawable.ic_profile, "Profile")
}