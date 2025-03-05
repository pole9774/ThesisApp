package it.polito.thesisapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import it.polito.thesisapp.navigation.Screen

/**
 * Composable function that displays the main FloatingActionButton (FAB).
 *
 * @param currentRoute The current route in the navigation.
 * @param onFabClick The callback to be invoked when the FAB is clicked.
 */
@Composable
fun MainFab(
    currentRoute: String?,
    onFabClick: () -> Unit
) {
    if (Screen.isHomeRoute(currentRoute) ||
        Screen.isTeamRoute(currentRoute) ||
        Screen.isCreateTeamRoute(currentRoute) ||
        Screen.isCreateTaskRoute(currentRoute)
    ) {
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            val icon =
                if (Screen.isCreateTeamRoute(currentRoute) || Screen.isCreateTaskRoute(currentRoute)) {
                    Icons.Default.Check
                } else {
                    Icons.Default.Add
                }

            Icon(
                imageVector = icon,
                contentDescription = getFabDescription(currentRoute)
            )
        }
    }
}

/**
 * Returns the content description for the FAB based on the current route.
 *
 * @param currentRoute The current route in the navigation.
 * @return The content description for the FAB.
 */
private fun getFabDescription(currentRoute: String?): String =
    when {
        Screen.isHomeRoute(currentRoute) -> "Create Team"
        Screen.isTeamRoute(currentRoute) -> "Create Task"
        Screen.isCreateTeamRoute(currentRoute) -> "Save Team"
        Screen.isCreateTaskRoute(currentRoute) -> "Save Task"
        else -> "Add"
    }
