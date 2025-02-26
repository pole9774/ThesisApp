package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.viewmodel.HomeViewModel

/**
 * @param userId The unique identifier of the user.
 * @param viewModel The ViewModel that manages the state of the home screen. Defaults to an instance provided by viewModel().
 */
@Composable
fun HomeScreen(
    userId: String,
    viewModel: HomeViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val teams by viewModel.teams.collectAsState()
    val pagerState = rememberPagerState { teams.size }
    val currentTeam = teams.getOrNull(pagerState.currentPage)

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WelcomeSection(profile)
        TeamSection(teams, pagerState)
        HorizontalDivider()
        TasksSection(currentTeam?.tasks ?: emptyList())
    }
}

/**
 * Composable function that displays the tasks section.
 *
 * @param tasks The list of tasks to display.
 */
@Composable
private fun TasksSection(tasks: List<Task>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.titleLarge
        )
        if (tasks.isEmpty()) {
            Text(
                text = "No tasks yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task)
                }
            }
        }
    }
}

/**
 * Composable function that displays a card for a task.
 *
 * @param task The task to display.
 */
@Composable
private fun TaskCard(task: Task) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${task.assignedMembers.size} assigned members",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * Composable function that displays the team section.
 *
 * @param teams The list of teams to display.
 * @param pagerState The state of the pager used to display the teams.
 */
@Composable
private fun TeamSection(
    teams: List<Team>,
    pagerState: PagerState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "My Teams",
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(
                horizontal = (LocalConfiguration.current.screenWidthDp.dp / 2) - 100.dp
            ),
            pageSpacing = 8.dp
        ) { page ->
            TeamCard(
                team = teams[page],
                modifier = Modifier
                    .padding(4.dp)
                    .width(200.dp)
            )
        }
    }
}

/**
 * Composable function that displays a card for a team.
 *
 * @param team The team to display.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
private fun TeamCard(
    team: Team,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${team.members.size} members",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Composable function that displays the welcome section.
 *
 * @param profile The profile of the user.
 */
@Composable
private fun WelcomeSection(profile: Profile?) {
    Text(
        text = "Welcome, ${profile?.firstName ?: "..."}",
        style = MaterialTheme.typography.headlineMedium
    )
}
