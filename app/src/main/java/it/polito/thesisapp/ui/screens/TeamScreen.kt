package it.polito.thesisapp.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.polito.thesisapp.R
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.ui.components.LoadingIndicator
import it.polito.thesisapp.viewmodel.TeamViewModel

@Composable
fun TeamScreen(
    teamId: String,
    viewModel: TeamViewModel = hiltViewModel(),
    onNavigateToTask: (String, String) -> Unit = { _, _ -> }
) {
    val team by viewModel.team.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sortMode by viewModel.taskSortMode.collectAsState()
    val sortedTasks by viewModel.sortedTasks.collectAsState()
    val selectedFilters by viewModel.selectedStatusFilters.collectAsState() // Get selected filters
    val lazyListState = rememberLazyListState()

    LaunchedEffect(teamId) {
        viewModel.loadTeam(teamId)
    }

    LaunchedEffect(sortMode, selectedFilters) {
        lazyListState.animateScrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                team?.let { currentTeam ->
                    Text(
                        text = currentTeam.name,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = currentTeam.members.size.toString() + " members",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tasks (" + sortedTasks.size + ")",
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(onClick = { viewModel.toggleSortMode() }) {
                        Icon(
                            painter = when (sortMode) {
                                TeamViewModel.TaskSortMode.DATE_DESC -> painterResource(R.drawable.sort_24)
                                TeamViewModel.TaskSortMode.NAME_ASC -> painterResource(R.drawable.arrow_upward_24)
                                TeamViewModel.TaskSortMode.NAME_DESC -> painterResource(R.drawable.arrow_downward_24)
                            },
                            contentDescription = "Sort tasks",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Add the FilterChipRow here
                FilterChipRow(
                    selectedFilters = selectedFilters,
                    onFilterToggle = { viewModel.toggleStatusFilter(it) }
                )

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
                                teamId = teamId,
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
    }
}

@Composable
private fun TaskCard(
    task: Task,
    teamId: String,
    modifier: Modifier = Modifier,
    onTaskClick: (String, String) -> Unit = { _, _ -> }
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

@Composable
private fun FilterChipRow(
    selectedFilters: Set<TaskStatus>,
    onFilterToggle: (TaskStatus) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskStatus.entries.forEach { status ->
            FilterChip(
                selected = status in selectedFilters,
                onClick = { onFilterToggle(status) },
                label = { Text(status.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    selectedContainerColor = Color(status.color).copy(alpha = 0.7f),
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                )
            )
        }
    }
}