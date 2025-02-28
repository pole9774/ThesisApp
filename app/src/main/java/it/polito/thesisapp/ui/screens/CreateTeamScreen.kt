package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.ui.LocalNavigationManager
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.viewmodel.AppViewModelProvider
import it.polito.thesisapp.viewmodel.CreateTeamViewModel

@Composable
fun CreateTeamScreen(
    navigationManager: NavigationManager = LocalNavigationManager.current,
    viewModel: CreateTeamViewModel = AppViewModelProvider.createTeamViewModel(),
    afterTeamCreated: () -> Unit = {},
) {
    var teamName by remember { mutableStateOf("") }
    val error by viewModel.error.collectAsState()

    LaunchedEffect(teamName) {
        navigationManager.setArgument(Constants.Navigation.Tags.TEAM_NAME, teamName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Create New Team",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Team Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.teamCreated.collect { success ->
            if (success) {
                afterTeamCreated()
            }
        }
    }
}