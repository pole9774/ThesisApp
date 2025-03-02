package it.polito.thesisapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * Manager class responsible for handling navigation operations throughout the app.
 * It abstracts the NavController and provides high-level navigation methods.
 *
 * @property navController The NavController instance used for navigation
 */
class NavigationManager(private val navController: NavController) {

    /**
     * Sealed class representing navigation events in the application.
     * Each event corresponds to a specific navigation action.
     */
    sealed class NavigationEvent {
        /**
         * Navigation event to navigate to the home screen.
         */
        data object NavigateToHome : NavigationEvent()

        /**
         * Navigation event to navigate to the home screen and clear the back stack.
         */
        data object NavigateToHomeWithClearBackStack : NavigationEvent()

        /**
         * Navigation event to navigate to a specific team screen.
         *
         * @property teamId The ID of the team to navigate to.
         */
        data class NavigateToTeam(val teamId: String) : NavigationEvent()

        /**
         * Navigation event to navigate to the create team screen.
         */
        data object NavigateToCreateTeam : NavigationEvent()

        /**
         * Navigation event to navigate to the create task screen for a specific team.
         *
         * @property teamId The ID of the team to create a task for.
         */
        data class NavigateToCreateTask(val teamId: String) : NavigationEvent()

        /**
         * Navigation event to navigate to the home screen after team creation.
         */
        data object NavigateToHomeAfterTeamCreation : NavigationEvent()

        /**
         * Navigation event to navigate to a specific team screen after task creation.
         *
         * @property teamId The ID of the team to navigate to.
         */
        data class NavigateToTeamAfterTaskCreation(val teamId: String) : NavigationEvent()

        /**
         * Navigation event to navigate to the profile screen.
         */
        data object NavigateToProfile : NavigationEvent()
    }

    /**
     * Handles different navigation events and routes them to appropriate navigation methods.
     * Provides a single entry point for all navigation actions.
     *
     * @param event The navigation event to handle.
     */
    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToHome -> navigateToHome(false)
            is NavigationEvent.NavigateToHomeWithClearBackStack -> navigateToHome(true)
            is NavigationEvent.NavigateToTeam -> navigateToTeam(event.teamId)
            is NavigationEvent.NavigateToCreateTeam -> navigateToCreateTeam()
            is NavigationEvent.NavigateToCreateTask -> navigateToCreateTask(event.teamId)
            is NavigationEvent.NavigateToHomeAfterTeamCreation -> navigateToHomeAfterTeamCreation()
            is NavigationEvent.NavigateToTeamAfterTaskCreation -> navigateToTeamAfterTaskCreation(
                event.teamId
            )

            is NavigationEvent.NavigateToProfile -> navigateToProfile()
        }
    }

    /**
     * Navigate to the home screen, optionally clearing the back stack.
     *
     * @param clearBackStack If true, removes all screens from the back stack.
     */
    private fun navigateToHome(clearBackStack: Boolean = false) {
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
    private fun navigateToTeam(teamId: String) {
        navController.navigate(Screen.buildTeamRoute(teamId))
    }

    /**
     * Navigate to the create team screen.
     */
    private fun navigateToCreateTeam() {
        navController.navigate(Screen.buildCreateTeamRoute())
    }

    /**
     * Navigate to home after team creation, clearing the back stack.
     */
    private fun navigateToHomeAfterTeamCreation() {
        navigateToHome(clearBackStack = true)
    }

    /**
     * Navigate to create task screen for a specific team.
     *
     * @param teamId The ID of the team to create a task for.
     */
    private fun navigateToCreateTask(teamId: String) {
        navController.navigate(Screen.buildCreateTaskRoute(teamId))
    }

    /**
     * Navigate to team screen after task creation, clearing the back stack.
     *
     * @param teamId The ID of the team to display.
     */
    private fun navigateToTeamAfterTaskCreation(teamId: String) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(Screen.buildTeamRoute(teamId), inclusive = true)
            .build()

        navController.navigate(Screen.buildTeamRoute(teamId), navOptions)
    }

    /**
     * Navigate to profile screen.
     */
    private fun navigateToProfile() {
        navController.navigate(Screen.buildProfileRoute())
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

    /**
     * Gets the current back stack entry.
     *
     * @return The current back stack entry or null.
     */
    fun getCurrentBackStackEntry() = navController.currentBackStackEntry

    /**
     * Get an argument from the current back stack entry.
     *
     * @param key The key of the argument to retrieve.
     * @return The argument string value or null.
     */
    fun getArgument(key: String): String? =
        navController.currentBackStackEntry?.arguments?.getString(key)

    /**
     * Gets the current route of the navigation controller.
     *
     * @return The current route string or null.
     */
    fun getCurrentRoute(): String? {
        return navController.currentBackStackEntry?.destination?.route
    }

    /**
     * A more readable alias for handleNavigationEvent.
     * Provides a clean, intuitive API for navigation.
     *
     * @param event The navigation event to handle.
     */
    fun navigate(event: NavigationEvent) {
        handleNavigationEvent(event)
    }

    /**
     * Navigates to a destination in the bottom navigation bar.
     *
     * @param screen The screen destination to navigate to.
     */
    fun navigateToBottomBarDestination(screen: Screen) {
        when (screen) {
            Screen.Home -> navigate(NavigationEvent.NavigateToHomeWithClearBackStack)
            Screen.Profile -> navigate(NavigationEvent.NavigateToProfile)
        }
    }
}