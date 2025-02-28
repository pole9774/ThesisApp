package it.polito.thesisapp.navigation

import android.util.Log
import it.polito.thesisapp.R

/**
 * Sealed class representing different screens in the application.
 * Uses companion object properties to avoid hardcoding route strings.
 */
sealed class Screen(val route: String, val icon: Int, val label: String) {
    companion object {
        /**
         * Route constant for the Home screen.
         */
        private const val HOME_ROUTE = "home/"

        private const val TEAM_ROUTE = "team/"

        private const val CREATE_TEAM_ROUTE = "create_team/"

        private const val CREATE_TASK_ROUTE = "create_task/"

        fun buildHomeRoute() = HOME_ROUTE
        fun buildTeamRoute(teamId: String) = TEAM_ROUTE + teamId
        fun buildCreateTeamRoute() = CREATE_TEAM_ROUTE
        fun buildCreateTaskRoute(teamId: String): String {
            Log.d("AAA", "teamId in createTaskRoute: $teamId")
            return CREATE_TASK_ROUTE + teamId
        }

        fun isHomeRoute(route: String?): Boolean {
            return route == HOME_ROUTE
        }

        fun isTeamRoute(route: String?): Boolean {
            return route?.startsWith(TEAM_ROUTE) == true
        }

        fun isCreateTeamRoute(route: String?): Boolean {
            return route?.startsWith(CREATE_TEAM_ROUTE) == true
        }

        fun isCreateTaskRoute(route: String?): Boolean {
            return route?.startsWith(CREATE_TASK_ROUTE) == true
        }

        fun getTeamId(route: String?): String? {
            return when {
                route?.startsWith(TEAM_ROUTE) == true -> route.substringAfter(TEAM_ROUTE)
                route?.startsWith(CREATE_TASK_ROUTE) == true -> route.substringAfter(
                    CREATE_TASK_ROUTE
                )

                else -> null
            }
        }

        val screensBottomBar = listOf(Home)
    }

    /**
     * Object representing the Home screen.
     */
    data object Home : Screen(HOME_ROUTE, R.drawable.ic_home, "Home")
}