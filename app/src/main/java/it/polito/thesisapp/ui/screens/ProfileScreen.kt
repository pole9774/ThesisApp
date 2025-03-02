package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.ui.components.LoadingIndicator
import it.polito.thesisapp.ui.components.UserMonogram
import it.polito.thesisapp.viewmodel.AppViewModelProvider
import it.polito.thesisapp.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Composable function that displays the profile screen.
 *
 * @param userId The unique identifier of the user.
 * @param viewModel The ViewModel that manages the state of the profile screen. Defaults to an instance provided by AppViewModelProvider.
 */
@Composable
fun ProfileScreen(
    userId: String,
    viewModel: ProfileViewModel = AppViewModelProvider.profileViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                profile?.let { userProfile ->
                    UserMonogram(
                        firstName = userProfile.firstName,
                        lastName = userProfile.lastName,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Text(
                        text = "${userProfile.firstName} ${userProfile.lastName}",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    ProfileInfoSection(userProfile)
                }
            }
        }
    }
}

/**
 * Composable function that displays the profile information section.
 *
 * @param profile The profile data to display.
 */
@Composable
private fun ProfileInfoSection(profile: Profile) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileInfoItem("Name", "${profile.firstName} ${profile.lastName}")

            val birthDateFormatted = remember {
                val date = profile.birthDate.toDate()
                SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(date)
            }

            ProfileInfoItem("Birth Date", birthDateFormatted)
            ProfileInfoItem("Teams", "${profile.teams.size} teams")
        }
    }
}

/**
 * Composable function that displays a profile information item.
 *
 * @param label The label of the information item.
 * @param value The value of the information item.
 */
@Composable
private fun ProfileInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
