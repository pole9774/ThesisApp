package it.polito.thesisapp.ui.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.ui.LocalNavigationManager
import it.polito.thesisapp.utils.Constants
import it.polito.thesisapp.viewmodel.CreateTaskViewModel

/**
 * Composable function that displays the screen for creating a new task.
 *
 * @param navigationManager The navigation manager to handle navigation actions.
 * @param viewModel The ViewModel for creating tasks.
 * @param afterTaskCreated Callback to be invoked after the task is created.
 */
@Composable
fun CreateTaskScreen(
    navigationManager: NavigationManager = LocalNavigationManager.current,
    viewModel: CreateTaskViewModel = hiltViewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity),
    afterTaskCreated: () -> Unit = {},
) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    LaunchedEffect(taskName, taskDescription) {
        navigationManager.setArgument(Constants.Navigation.Tags.TASK_NAME, taskName)
        navigationManager.setArgument(Constants.Navigation.Tags.TASK_DESCRIPTION, taskDescription)
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
                text = "Create New Task",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.taskCreated.collect { success ->
            if (success) {
                afterTaskCreated()
            }
        }
    }
}