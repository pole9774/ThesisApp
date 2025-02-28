package it.polito.thesisapp.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * Central class for handling all navigation-related actions across the app.
 * Provides type-safe navigation methods and encapsulates navigation logic.
 */
class NavigationManager(private val navController: NavController) {

    /**
     * Navigate to the home screen, optionally clearing the back stack.
     *
     * @param clearBackStack If true, removes all screens from the back stack.
     */
    fun navigateToHome(clearBackStack: Boolean = false) {
        val navOptions = if (clearBackStack) {
            NavOptions.Builder()
                .setPopUpTo(Screen.buildHomeRoute(), inclusive = true)
                .build()
        } else null

        navController.navigate(Screen.buildHomeRoute(), navOptions)
    }

    /**
     * Navigate to the team detail screen for a specific team.
     *
     * @param teamId The ID of the team to display.
     */
    fun navigateToTeam(teamId: String) {
        Log.d("AAA", "teamId in navigateToTeam: $teamId")
        navController.navigate(Screen.buildTeamRoute(teamId))
    }

    /**
     * Navigate to the create team screen.
     */
    fun navigateToCreateTeam() {
        navController.navigate(Screen.buildCreateTeamRoute())
    }

    /**
     * Navigate to home after team creation, clearing the back stack.
     */
    fun navigateToHomeAfterTeamCreation() {
        navigateToHome(clearBackStack = true)
    }

    fun navigateToCreateTask(teamId: String) {
        Log.d("AAA", "teamId in navigateToCreateTask: $teamId")
        navController.navigate(Screen.buildCreateTaskRoute(teamId))
    }

    fun navigateToTeamAfterTaskCreation(teamId: String) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(Screen.buildTeamRoute(teamId), inclusive = true)
            .build()

        navController.navigate(Screen.buildTeamRoute(teamId), navOptions)
    }

    /**
     * Get the current value from SavedStateHandle or null if not present.
     *
     * @param T The type of value to retrieve.
     * @param key The key used to store the value.
     * @return The stored value of type T or null if not present.
     */
    fun <T> getCurrentArgument(key: String): T? {
        return navController.currentBackStackEntry?.savedStateHandle?.get<T>(key)
    }

    /**
     * Set a value in the current SavedStateHandle.
     *
     * @param T The type of value to store.
     * @param key The key to use for storing the value.
     * @param value The value to store.
     */
    fun <T> setArgument(key: String, value: T) {
        navController.currentBackStackEntry?.savedStateHandle?.set(key, value)
    }

    fun getCurrentBackStackEntry() = navController.currentBackStackEntry

    fun getArgument(key: String): String? =
        navController.currentBackStackEntry?.arguments?.getString(key)
}