package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.ui.components.LoadingIndicator
import it.polito.thesisapp.viewmodel.AppViewModelProvider
import it.polito.thesisapp.viewmodel.TaskViewModel

@Composable
fun TaskScreen(
    teamId: String,
    taskId: String,
    viewModel: TaskViewModel = AppViewModelProvider.taskViewModel()
) {
    val task by viewModel.task.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(teamId, taskId) {
        viewModel.loadTask(teamId, taskId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                task?.let { taskData ->
                    TaskHeader(
                        task = taskData,
                        teamId = teamId,
                        viewModel = viewModel,
                        
                        )
                    HorizontalDivider()
                    TaskDescription(taskData.description)
                    HorizontalDivider()
                    AssignedMembers(taskData)
                }
            }
        }
    }
}

@Composable
private fun TaskHeader(
    task: Task,
    teamId: String,
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = task.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = task.description,
            style = MaterialTheme.typography.bodyLarge
        )

        // Add TaskStatusSelector here
        TaskStatusSelector(
            currentStatus = task.status,
            onStatusChanged = { newStatus ->
                viewModel.updateTaskStatus(teamId, task.id, newStatus)
            }
        )
    }
}

@Composable
private fun TaskDescription(description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun AssignedMembers(task: Task) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Assigned Members",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${task.assignedMembers.size} members assigned",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TaskStatusSelector(
    currentStatus: TaskStatus,
    onStatusChanged: (TaskStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = Color(currentStatus.color),
            contentColor = Color.White,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = currentStatus.displayName,
                    style = MaterialTheme.typography.labelMedium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Change status",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TaskStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(status.color), CircleShape)
                                    .padding(end = 8.dp)
                            )
                            Text(status.displayName)
                        }
                    },
                    onClick = {
                        onStatusChanged(status)
                        expanded = false
                    },
                    trailingIcon = {
                        if (status == currentStatus) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected"
                            )
                        }
                    }
                )
            }
        }
    }
}