package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.polito.thesisapp.ui.components.LoadingIndicator
import it.polito.thesisapp.viewmodel.AppViewModelProvider
import it.polito.thesisapp.viewmodel.TeamViewModel

/**
 * Composable function that displays the team screen.
 *
 * @param teamId The unique identifier of the team.
 * @param viewModel The ViewModel that manages the state of the team screen. Defaults to an instance provided by AppViewModelProvider.
 */
@Composable
fun TeamScreen(
    teamId: String,
    viewModel: TeamViewModel = AppViewModelProvider.teamViewModel()
) {
    val team by viewModel.team.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.loadTeam(teamId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = team?.name ?: "Team not found",
                    style = MaterialTheme.typography.headlineMedium
                )

                team?.description?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}