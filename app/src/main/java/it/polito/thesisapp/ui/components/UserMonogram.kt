package it.polito.thesisapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function that displays a user's monogram (initials) inside a circular shape.
 *
 * @param firstName The first name of the user.
 * @param lastName The last name of the user.
 * @param modifier Modifier to be applied to the Box layout.
 * @param size The size of the circular shape in dp.
 */
@Composable
fun UserMonogram(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    size: Int = 80
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildInitials(firstName, lastName),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/**
 * Builds the initials from the first and last name.
 *
 * @param firstName The first name of the user.
 * @param lastName The last name of the user.
 * @return A string containing the initials.
 */
private fun buildInitials(firstName: String, lastName: String): String {
    val firstInitial = firstName.firstOrNull()?.uppercase() ?: ""
    val lastInitial = lastName.firstOrNull()?.uppercase() ?: ""
    return "$firstInitial$lastInitial"
}
