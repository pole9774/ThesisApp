package it.polito.thesisapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function that displays a loading indicator.
 *
 * @param modifier Modifier to be applied to the Box layout.
 * @param size Size of the CircularProgressIndicator in dp.
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Int = 40
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}