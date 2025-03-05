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

        /**
         * Builds the route for the task screen with the specified team ID and task ID.
         *
         * @param teamId The ID of the team.
         * @param taskId The ID of the task.
         * @return The route string for the task screen.
         */
        fun buildTaskRoute(teamId: String, taskId: String) =
            Constants.Navigation.Routes.TASK
                .replace("{${Constants.Navigation.Params.TEAM_ID}}", teamId)
                .replace("{${Constants.Navigation.Params.TASK_ID}}", taskId)

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