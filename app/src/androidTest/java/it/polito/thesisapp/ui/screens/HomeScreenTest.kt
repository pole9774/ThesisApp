package it.polito.thesisapp.ui.screens

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.DpRect
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.model.TeamMember
import it.polito.thesisapp.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Date

/**
 * Custom assertion that verifies this node is positioned above another node in the UI.
 *
 * @param other The node that should be positioned below this node
 * @return This node for method chaining
 */
fun SemanticsNodeInteraction.assertIsAbove(other: SemanticsNodeInteraction): SemanticsNodeInteraction {
    val thisBounds: DpRect = this.getBoundsInRoot()
    val otherBounds: DpRect = other.getBoundsInRoot()

    if (thisBounds.bottom > otherBounds.top) {
        throw AssertionError(
            "Expected node to be above other node.\n" +
                    "This node bottom: ${thisBounds.bottom}, Other node top: ${otherBounds.top}"
        )
    }

    return this
}

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test constants
    private val testUserId = "test-user"

    // Test fixtures
    private lateinit var mockViewModel: HomeViewModel
    private val defaultTeams = listOf(
        createTeam("1", "Team A", "AAA"),
        createTeam("2", "Team B", "BBB"),
        createTeam("3", "Team C", "CCC")
    )

    @Before
    fun setUp() {
        mockViewModel = createMockViewModel(defaultTeams)
    }

    @Test
    fun horizontalPager_whenSwiped_displaysNextTeamAndUpdatesSelection() {
        // Arrange
        renderHomeScreen()

        // Assert initial state
        composeTestRule.onNodeWithText("Team A").assertIsDisplayed()

        // Act: Swipe to next team
        composeTestRule.onNode(hasText("Team A")).performTouchInput {
            swipeLeft()
        }

        // Assert
        composeTestRule.onNodeWithText("Team B").assertIsDisplayed()
        verify { mockViewModel.selectTeam(1) }
    }

    @Test
    fun teamCard_whenClicked_navigatesToTeamDetails() {
        // Arrange
        var navigatedTeamId = ""
        val singleTeam = listOf(createTeam("1", "Team A"))
        mockViewModel = createMockViewModel(singleTeam)

        // Act
        renderHomeScreen(onNavigateToTeam = { teamId -> navigatedTeamId = teamId })

        composeTestRule.onNodeWithText("Team A").performClick()

        // Assert
        assert(navigatedTeamId == "1") { "Expected navigation to team '1', but got '$navigatedTeamId'" }
    }

    @Test
    fun sortButton_whenClicked_callsToggleSortMode() {
        // Arrange
        val tasks = listOf(
            createTask("1", "Task A"),
            createTask("2", "Task B")
        )
        val teamsWithTasks = listOf(createTeam("1", "Team A", tasks = tasks))
        mockViewModel = createMockViewModel(teamsWithTasks)
        every { mockViewModel.sortedTasks } returns MutableStateFlow(tasks)

        // Act
        renderHomeScreen()
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()

        // Assert
        verify { mockViewModel.toggleSortMode() }
    }

    @Test
    fun taskList_whenSortModeChanges_displaysTasksInCorrectOrder() {
        // Arrange
        val tasksByDateDesc = listOf(
            createTask("1", "Task B", creationDate = Timestamp(Date.valueOf("2023-5-3"))),
            createTask("2", "Task A", creationDate = Timestamp(Date.valueOf("2023-5-2"))),
            createTask("3", "Task C", creationDate = Timestamp(Date.valueOf("2023-5-1")))
        )
        val tasksByNameAsc = listOf(
            createTask("2", "Task A", creationDate = Timestamp(Date.valueOf("2023-5-2"))),
            createTask("1", "Task B", creationDate = Timestamp(Date.valueOf("2023-5-3"))),
            createTask("3", "Task C", creationDate = Timestamp(Date.valueOf("2023-5-1")))
        )
        val tasksByNameDesc = listOf(
            createTask("3", "Task C", creationDate = Timestamp(Date.valueOf("2023-5-1"))),
            createTask("1", "Task B", creationDate = Timestamp(Date.valueOf("2023-5-3"))),
            createTask("2", "Task A", creationDate = Timestamp(Date.valueOf("2023-5-2")))
        )

        // Create StateFlows we can update during the test
        val tasksFlow = MutableStateFlow(tasksByDateDesc)
        val sortModeFlow = MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)

        mockViewModel = mockk<HomeViewModel>(relaxed = true)
        every { mockViewModel.teams } returns MutableStateFlow(listOf(createTeam("1", "Team")))
        every { mockViewModel.selectedTeamIndex } returns MutableStateFlow(0)
        every { mockViewModel.isLoading } returns MutableStateFlow(false)
        every { mockViewModel.profile } returns MutableStateFlow(null)
        every { mockViewModel.sortedTasks } returns tasksFlow
        every { mockViewModel.taskSortMode } returns sortModeFlow

        // Set up toggle behavior to cycle through all three states
        every { mockViewModel.toggleSortMode() } answers {
            when (sortModeFlow.value) {
                HomeViewModel.TaskSortMode.DATE_DESC -> {
                    sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_ASC
                    tasksFlow.value = tasksByNameAsc
                }

                HomeViewModel.TaskSortMode.NAME_ASC -> {
                    sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_DESC
                    tasksFlow.value = tasksByNameDesc
                }

                HomeViewModel.TaskSortMode.NAME_DESC -> {
                    sortModeFlow.value = HomeViewModel.TaskSortMode.DATE_DESC
                    tasksFlow.value = tasksByDateDesc
                }
            }
        }

        // Act & Assert initial state (DATE_DESC)
        renderHomeScreen()

        composeTestRule.onNodeWithText("Task B").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task B")
            .assertIsAbove(composeTestRule.onNodeWithText("Task A"))
            .assertIsAbove(composeTestRule.onNodeWithText("Task C"))

        // Click sort button to switch to NAME_ASC
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
        composeTestRule.waitForIdle()

        // Assert alphabetical ascending order
        composeTestRule.onNodeWithText("Task A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task A")
            .assertIsAbove(composeTestRule.onNodeWithText("Task B"))
            .assertIsAbove(composeTestRule.onNodeWithText("Task C"))

        // Click sort button to switch to NAME_DESC
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
        composeTestRule.waitForIdle()

        // Assert alphabetical descending order
        composeTestRule.onNodeWithText("Task C").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task C")
            .assertIsAbove(composeTestRule.onNodeWithText("Task B"))
            .assertIsAbove(composeTestRule.onNodeWithText("Task A"))

        // Click sort button to return to DATE_DESC
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
        composeTestRule.waitForIdle()

        // Assert back to creation date descending order
        composeTestRule.onNodeWithText("Task B").assertIsDisplayed()
        composeTestRule.onNodeWithText("Task B")
            .assertIsAbove(composeTestRule.onNodeWithText("Task A"))
            .assertIsAbove(composeTestRule.onNodeWithText("Task C"))
    }

    @Test
    fun taskList_whenSortingByNameDesc_displaysTasksInReverseAlphabeticalOrder() {
        // Arrange
        val tasksByNameDesc = listOf(
            createTask("1", "Task B"),
            createTask("2", "Task A")
        )

        mockViewModel = createMockViewModel(listOf(createTeam("1", "Team")))
        every { mockViewModel.sortedTasks } returns MutableStateFlow(tasksByNameDesc)
        every { mockViewModel.taskSortMode } returns MutableStateFlow(HomeViewModel.TaskSortMode.NAME_DESC)

        // Act
        renderHomeScreen()

        // Assert
        composeTestRule.onNodeWithText("Task B")
            .assertIsAbove(composeTestRule.onNodeWithText("Task A"))
    }

    // Helper methods
    private fun renderHomeScreen(
        onNavigateToTeam: (String) -> Unit = {},
        onNavigateToTask: (String, String) -> Unit = { _, _ -> }
    ) {
        composeTestRule.setContent {
            HomeScreen(
                userId = testUserId,
                viewModel = mockViewModel,
                onNavigateToTeam = onNavigateToTeam,
                onNavigateToTask = onNavigateToTask
            )
        }
    }

    private fun createTeam(
        id: String,
        name: String,
        description: String = "",
        members: List<TeamMember> = emptyList(),
        tasks: List<Task> = emptyList()
    ): Team {
        return Team(
            id = id,
            name = name,
            description = description,
            members = members,
            tasks = tasks
        )
    }

    private fun createMockViewModel(teams: List<Team>): HomeViewModel {
        val mockViewModel = mockk<HomeViewModel>(relaxed = true)
        every { mockViewModel.teams } returns MutableStateFlow(teams)
        every { mockViewModel.selectedTeamIndex } returns MutableStateFlow(0)
        every { mockViewModel.isLoading } returns MutableStateFlow(false)
        every { mockViewModel.profile } returns MutableStateFlow(null)
        every { mockViewModel.sortedTasks } returns MutableStateFlow(emptyList())
        every { mockViewModel.taskSortMode } returns MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)
        return mockViewModel
    }

    private fun createTask(
        id: String,
        name: String,
        description: String = "",
        creationDate: Timestamp = Timestamp.now(),
        status: TaskStatus = TaskStatus.TODO
    ): Task {
        return Task(
            id = id,
            name = name,
            description = description,
            creationDate = creationDate,
            assignedMembers = emptyList(),
            status = status
        )
    }
}
