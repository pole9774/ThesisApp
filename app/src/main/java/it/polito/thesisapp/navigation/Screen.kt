package it.polito.thesisapp.navigation

import it.polito.thesisapp.R
import it.polito.thesisapp.utils.Constants

/**
 * Sealed class representing different screens in the application.
 * Uses companion object properties to avoid hardcoding route strings.
 */
sealed class Screen(val route: String, val icon: Int, val label: String) {
    companion object {
        fun buildHomeRoute() = Constants.Navigation.Routes.HOME
        fun buildTeamRoute(teamId: String) = Constants.Navigation.Routes.TEAM.replace(
            "{${Constants.Navigation.Params.TEAM_ID}}",
            teamId
        )

        fun buildCreateTeamRoute() = Constants.Navigation.Routes.CREATE_TEAM
        fun buildCreateTaskRoute(teamId: String): String =
            Constants.Navigation.Routes.CREATE_TASK.replace(
                "{${Constants.Navigation.Params.TEAM_ID}}",
                teamId
            )

        fun isHomeRoute(route: String?): Boolean {
            return route == Constants.Navigation.Routes.HOME
        }

        fun isTeamRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.TEAM.substringBefore("{")) == true
        }

        fun isCreateTeamRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.CREATE_TEAM) == true
        }

        fun isCreateTaskRoute(route: String?): Boolean {
            return route?.startsWith(Constants.Navigation.Routes.CREATE_TASK.substringBefore("{")) == true
        }

        val screensBottomBar = listOf(Home)
    }

    /**
     * Object representing the Home screen.
     */
    data object Home : Screen(Constants.Navigation.Routes.HOME, R.drawable.ic_home, "Home")
}