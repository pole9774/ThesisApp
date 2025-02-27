package it.polito.thesisapp.navigation

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
        const val HOME_ROUTE = "home"

        /**
         * Route constant for the Team screen.
         * Contains a dynamic path parameter 'teamId' to identify specific teams.
         */
        const val TEAM_ROUTE = "team/{teamId}"

        const val CREATE_TEAM_ROUTE = "create_team"

        /**
         * Constructs a route for a specific team screen.
         * @param teamId The unique identifier of the team.
         * @return A formatted route string in the pattern "team/{teamId}".
         */
        fun teamRoute(teamId: String) = "team/$teamId"


        val screensBottomBar = listOf(Home)
    }

    /**
     * Object representing the Home screen.
     */
    data object Home : Screen(HOME_ROUTE, R.drawable.ic_home, "Home")
    data object Team : Screen(TEAM_ROUTE, 0, "Team")
    data object CreateTeam : Screen(CREATE_TEAM_ROUTE, 0, "Create Team")
}