package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import it.polito.thesisapp.viewmodel.AppViewModelProvider
import it.polito.thesisapp.viewmodel.TeamViewModel

@Composable
fun TeamScreen(
    teamId: String,
    viewModel: TeamViewModel = AppViewModelProvider.teamViewModel()
) {
    val team by viewModel.team.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.loadTeam(teamId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = team?.name ?: "Loading...",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}