package it.polito.thesisapp.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.navigation.NavigationManager.NavigationEvent
import it.polito.thesisapp.ui.LocalNavigationManager
import it.polito.thesisapp.ui.components.LoadingIndicator
import it.polito.thesisapp.ui.components.ScaffoldWithFab
import it.polito.thesisapp.viewmodel.HomeViewModel

/**
 * Composable function that displays the home screen.
 *
 * @param userId The unique identifier of the user.
 * @param viewModel The ViewModel that manages the state of the home screen. Defaults to an instance provided by viewModel().
 * @param onNavigateToTeam Callback to be invoked when navigating to a team.
 */
@Composable
fun HomeScreen(
    userId: String,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToTeam: (String) -> Unit,
    onNavigateToTask: (String, String) -> Unit
) {
    val navigationManager = LocalNavigationManager.current
    val profile by viewModel.profile.collectAsState()
    val teams by viewModel.teams.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val pagerState = rememberPagerState { teams.size }
    teams.getOrNull(pagerState.currentPage)

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.selectTeam(pagerState.currentPage)
    }

    ScaffoldWithFab(
        icon = Icons.Default.Add,
        contentDescription = "Create Team",
        onFabClick = {
            navigationManager.navigate(NavigationEvent.NavigateToCreateTeam)
        }
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                LoadingIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WelcomeSection(profile)
                    TeamSection(
                        teams,
                        pagerState,
                        onTeamClick = onNavigateToTeam
                    )
                    HorizontalDivider()
                    TasksSection(
                        viewModel = viewModel,
                        onNavigateToTask = onNavigateToTask
                    )
                }
            }
        }
    }
}

/**
 * Composable function that displays the tasks section.
 *
 * @param viewModel The ViewModel that manages the state of the tasks section.
 */
@Composable
private fun TasksSection(
    viewModel: HomeViewModel,
    onNavigateToTask: (String, String) -> Unit
) {
    val sortMode by viewModel.taskSortMode.collectAsState()
    val sortedTasks by viewModel.sortedTasks.collectAsState()
    val lazyListState = rememberLazyListState()
    val teams by viewModel.teams.collectAsState()
    val selectedTeamIndex by viewModel.selectedTeamIndex.collectAsState()
    val currentTeam = teams.getOrNull(selectedTeamIndex)

    LaunchedEffect(sortMode) {
        lazyListState.animateScrollToItem(0)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (sortedTasks.isEmpty()) {
            Text(
                text = "No tasks yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = sortedTasks,
                    key = { task -> task.id }
                ) { task ->
                    TaskCard(
                        task = task,
                        teamId = currentTeam?.id ?: "",
                        modifier = Modifier.animateItem(
                            fadeInSpec = null, fadeOutSpec = null, placementSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        ),
                        onTaskClick = onNavigateToTask
                    )
                }
            }
        }
    }
}

/**
 * Composable function that displays a card for a task.
 *
 * @param task The task to display.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
private fun TaskCard(
    task: Task,
    teamId: String,
    modifier: Modifier = Modifier,
    onTaskClick: (String, String) -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { onTaskClick(teamId, task.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium
                )

                TaskStatusChip(status = task.status)
            }

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
 * Displays a chip showing the status of a task.
 *
 * This composable creates a colored surface based on the task's status color
 * and displays the status name inside it. The background color is taken from
 * the status object, while the text is always white for contrast.
 *
 * @param status The TaskStatus object containing the display name and color to be shown
 */
@Composable
private fun TaskStatusChip(status: TaskStatus) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = Color(status.color),
        contentColor = Color.White,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * Composable function that displays the team section.
 *
 * @param teams The list of teams to display.
 * @param pagerState The state of the pager used to display the teams.
 * @param onTeamClick Callback to be invoked when a team is clicked.
 */
@Composable
private fun TeamSection(
    teams: List<Team>,
    pagerState: PagerState,
    onTeamClick: (String) -> Unit
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
                    .width(200.dp),
                onTeamClick = onTeamClick
            )
        }
    }
}

/**
 * Composable function that displays a card for a team.
 *
 * @param team The team to display.
 * @param modifier The modifier to be applied to the card.
 * @param onTeamClick Callback to be invoked when the card is clicked.
 */
@Composable
private fun TeamCard(
    team: Team,
    modifier: Modifier = Modifier,
    onTeamClick: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onTeamClick(team.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
