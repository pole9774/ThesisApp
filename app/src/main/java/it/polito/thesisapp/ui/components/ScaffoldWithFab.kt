package it.polito.thesisapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A reusable Scaffold component with a Floating Action Button.
 *
 * @param icon The icon to display in the FAB.
 * @param contentDescription The content description for the FAB icon.
 * @param onFabClick The action to perform when the FAB is clicked.
 * @param content The content to display inside the Scaffold.
 */
@Composable
fun ScaffoldWithFab(
    icon: ImageVector,
    contentDescription: String,
    onFabClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = content
    )
}