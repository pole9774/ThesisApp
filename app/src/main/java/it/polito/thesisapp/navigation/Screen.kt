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
         * List of all screens in the application.
         */
        val screens = listOf(Home)
    }

    /**
     * Object representing the Home screen.
     */
    data object Home : Screen(HOME_ROUTE, R.drawable.ic_home, "Home")
}