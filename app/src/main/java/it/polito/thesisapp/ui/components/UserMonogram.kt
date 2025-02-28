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

private fun buildInitials(firstName: String, lastName: String): String {
    val firstInitial = firstName.firstOrNull()?.uppercase() ?: ""
    val lastInitial = lastName.firstOrNull()?.uppercase() ?: ""
    return "$firstInitial$lastInitial"
}
