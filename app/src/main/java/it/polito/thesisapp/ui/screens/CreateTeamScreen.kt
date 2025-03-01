package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.ui.LocalNavigationManager
import it.polito.thesisapp.ui.components.LoadingIndicator
import it.polito.thesisapp.ui.components.UserMonogram
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
    var teamDescription by remember { mutableStateOf("") }
    val error by viewModel.error.collectAsState()
    val allProfiles by viewModel.allProfiles.collectAsState()
    val selectedProfileIds by viewModel.selectedProfileIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllProfiles()
    }

    LaunchedEffect(teamName, teamDescription) {
        navigationManager.setArgument(Constants.Navigation.Tags.TEAM_NAME, teamName)
        navigationManager.setArgument(Constants.Navigation.Tags.TEAM_DESCRIPTION, teamDescription)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Create New Team",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = teamName,
                        onValueChange = { teamName = it },
                        label = { Text("Team Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrectEnabled = false,
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = teamDescription,
                        onValueChange = { teamDescription = it },
                        label = { Text("Team Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 7,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrectEnabled = true
                        )
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Select Team Members",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(allProfiles) { profile ->
                    ProfileCheckboxItem(
                        profile = profile,
                        isSelected = profile.id in selectedProfileIds,
                        isCurrentUser = profile.id == Constants.User.USER_ID,
                        onSelectionChanged = { selected ->
                            viewModel.toggleProfileSelection(profile.id, selected)
                        }
                    )
                }

                item {
                    error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
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

@Composable
fun ProfileCheckboxItem(
    profile: Profile,
    isSelected: Boolean,
    isCurrentUser: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = {
                if (!isCurrentUser) { // Don't allow current user to be unchecked
                    onSelectionChanged(it)
                }
            },
            enabled = !isCurrentUser // Disable checkbox for current user
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserMonogram(
                firstName = profile.firstName,
                lastName = profile.lastName,
                size = 36
            )

            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = "${profile.firstName} ${profile.lastName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (isCurrentUser) {
                    Text(
                        text = "(You - Admin)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}