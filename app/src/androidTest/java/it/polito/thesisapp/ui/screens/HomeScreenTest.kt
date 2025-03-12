package it.polito.thesisapp.ui.screens

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
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
import it.polito.thesisapp.model.Profile
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.TaskStatus
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.model.TeamMember
import it.polito.thesisapp.navigation.NavigationManager
import it.polito.thesisapp.navigation.NavigationManager.NavigationEvent
import it.polito.thesisapp.ui.LocalNavigationManager
import it.polito.thesisapp.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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

/**
 * Test class for HomeScreen composable.
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock dependencies
    private lateinit var mockViewModel: HomeViewModel
    private lateinit var mockNavigationManager: NavigationManager

    // Navigation tracking
    private var navigatedTeamId = ""
    private var navigatedTaskId = ""

    // Test data
    private val testUserId = "user123"
    private val testProfile = Profile(
        id = testUserId,
        firstName = "John",
        lastName = "Doe",
        birthDate = Timestamp.now(),
        teams = emptyList()
    )

    private val testTeams = listOf(
        Team(
            id = "team1",
            name = "Team Alpha",
            description = "First test team",
            members = listOf(TeamMember("ADMIN", mockk())),
            tasks = emptyList()
        ),
        Team(
            id = "team2",
            name = "Team Beta",
            description = "Second test team",
            members = listOf(TeamMember("MEMBER", mockk()), TeamMember("MEMBER", mockk())),
            tasks = emptyList()
        )
    )

    private val testTasks = listOf(
        Task(
            id = "task1",
            name = "First Task",
            description = "This is task 1",
            creationDate = Timestamp.now(),
            status = TaskStatus.TODO,
            assignedMembers = emptyList()
        ),
        Task(
            id = "task2",
            name = "Second Task",
            description = "This is task 2",
            creationDate = Timestamp(Timestamp.now().seconds - 3600, 0),
            status = TaskStatus.IN_PROGRESS,
            assignedMembers = listOf(mockk(), mockk())
        )
    )

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setUp() {
        mockViewModel = mockk(relaxed = true)
        mockNavigationManager = mockk(relaxed = true)
        resetNavigationState()
        setupDefaultMocks()
    }

    /**
     * Resets tracking variables for navigation.
     */
    private fun resetNavigationState() {
        navigatedTeamId = ""
        navigatedTaskId = ""
    }

    /**
     * Sets up default mock behavior.
     */
    private fun setupDefaultMocks() {
        every { mockViewModel.profile } returns MutableStateFlow(testProfile)
        every { mockViewModel.teams } returns MutableStateFlow(testTeams)
        every { mockViewModel.isLoading } returns MutableStateFlow(false)
        every { mockViewModel.sortedTasks } returns MutableStateFlow(testTasks)
        every { mockViewModel.taskSortMode } returns MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)
        every { mockViewModel.selectedTeamIndex } returns MutableStateFlow(0)
    }

    /**
     * Helper method to set up the HomeScreen composable.
     *
     * @param captureTeamNavigation Whether to capture team navigation events.
     * @param captureTaskNavigation Whether to capture task navigation events.
     */
    private fun setupHomeScreen(
        captureTeamNavigation: Boolean = false,
        captureTaskNavigation: Boolean = false
    ) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalNavigationManager provides mockNavigationManager) {
                HomeScreen(
                    userId = testUserId,
                    viewModel = mockViewModel,
                    onNavigateToTeam = { teamId ->
                        if (captureTeamNavigation) navigatedTeamId = teamId
                    },
                    onNavigateToTask = { teamId, taskId ->
                        if (captureTaskNavigation) {
                            navigatedTeamId = teamId
                            navigatedTaskId = taskId
                        }
                    }
                )
            }
        }
    }

    /**
     * Tests that the loading indicator is displayed when the loading state is true.
     */
    @Test
    fun loadingIndicator_isDisplayed_whenLoading() {
        // Given
        every { mockViewModel.isLoading } returns MutableStateFlow(true)

        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Welcome, John").assertDoesNotExist()
    }

    /**
     * Tests that the welcome message with the user's first name is correctly displayed.
     */
    @Test
    fun welcomeMessage_displaysUserFirstName() {
        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("Welcome, John").assertIsDisplayed()
    }

    /**
     * Tests that the welcome message shows a placeholder when profile is null.
     */
    @Test
    fun welcomeMessage_showsPlaceholder_whenProfileIsNull() {
        // Given
        every { mockViewModel.profile } returns MutableStateFlow(null)

        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("Welcome, ...").assertIsDisplayed()
    }

    /**
     * Tests that the teams are correctly displayed in the team section.
     */
    @Test
    fun teamSection_displaysTeams() {
        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("Team Alpha").assertIsDisplayed()
        composeTestRule.onNodeWithText("1 members").assertIsDisplayed()
    }

    /**
     * Tests that the "No tasks yet" message is shown when there are no tasks.
     */
    @Test
    fun taskSection_showsEmptyMessage_whenNoTasks() {
        // Given
        every { mockViewModel.sortedTasks } returns MutableStateFlow(emptyList())

        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("No tasks yet").assertIsDisplayed()
    }

    /**
     * Tests that tasks are correctly displayed in the task section.
     */
    @Test
    fun taskSection_displaysTasks() {
        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("First Task").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second Task").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is task 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is task 2").assertIsDisplayed()
    }

    /**
     * Tests that task status chips are correctly displayed with the right status text.
     */
    @Test
    fun taskStatusChips_areCorrectlyDisplayed() {
        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("To Do").assertIsDisplayed()
        composeTestRule.onNodeWithText("In Progress").assertIsDisplayed()
    }

    /**
     * Tests that clicking on a team triggers the correct navigation callback.
     */
    @Test
    fun teamCard_clickTriggersNavigation() {
        // When
        setupHomeScreen(captureTeamNavigation = true)
        composeTestRule.onNodeWithText("Team Alpha").performClick()

        // Then
        assert(navigatedTeamId == "team1")
    }

    /**
     * Tests that clicking on a task triggers the correct navigation callback.
     */
    @Test
    fun taskCard_clickTriggersNavigation() {
        // When
        setupHomeScreen(captureTaskNavigation = true)
        composeTestRule.onNodeWithText("First Task").performClick()

        // Then
        assert(navigatedTeamId == "team1")
        assert(navigatedTaskId == "task1")
    }

    /**
     * Tests that clicking the sort button calls the toggleSortMode function in the ViewModel.
     */
    @Test
    fun sortButton_callsToggleSortMode() {
        // When
        setupHomeScreen()
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()

        // Then
        verify(exactly = 1) { mockViewModel.toggleSortMode() }
    }

    /**
     * Tests that clicking the FAB button navigates to the create team screen.
     */
    @Test
    fun fabButton_navigatesToCreateTeam() {
        // When
        setupHomeScreen()
        composeTestRule.onNodeWithContentDescription("Create Team").performClick()

        // Then
        verify(exactly = 1) { mockNavigationManager.navigate(NavigationEvent.NavigateToCreateTeam) }
    }

    /**
     * Tests that the loadProfile function is called with the correct user ID when the screen is composed.
     */
    @Test
    fun homeScreen_callsLoadProfileWithUserId() {
        // When
        setupHomeScreen()

        // Then
        verify(exactly = 1) { mockViewModel.loadProfile(testUserId) }
    }

    /**
     * Tests the correct vertical layout structure of the home screen.
     */
    @Test
    fun homeScreen_hasCorrectVerticalLayoutStructure() {
        // When
        setupHomeScreen()

        // Then - verify elements are stacked in the correct order
        composeTestRule.onNodeWithText("Welcome, John")
            .assertIsAbove(composeTestRule.onNodeWithText("My Teams"))
        composeTestRule.onNodeWithText("My Teams")
            .assertIsAbove(composeTestRule.onNodeWithText("Tasks"))
    }

    /**
     * Tests that different sort modes display different sort icons.
     */
    @Test
    fun sortButton_displaysCorrectIcon() {
        // Create a mutable state flow that we can update
        val sortModeFlow = MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)
        every { mockViewModel.taskSortMode } returns sortModeFlow

        // Set up the HomeScreen
        setupHomeScreen()

        // For DATE_DESC mode
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Sort tasks").assertIsDisplayed()
        composeTestRule.onNodeWithTag("sort_icon_date_desc", useUnmergedTree = true).assertExists().assertIsDisplayed()

        // For NAME_ASC mode
        sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_ASC
        composeTestRule.waitForIdle()
        // Allow time for recomposition
        composeTestRule.mainClock.advanceTimeBy(300)
        composeTestRule.onNodeWithTag("sort_icon_name_asc", useUnmergedTree = true).assertExists().assertIsDisplayed()

        // For NAME_DESC mode
        sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_DESC
        composeTestRule.waitForIdle()
        // Allow time for recomposition
        composeTestRule.mainClock.advanceTimeBy(300)
        composeTestRule.onNodeWithTag("sort_icon_name_desc", useUnmergedTree = true).assertExists().assertIsDisplayed()
    }

    /**
     * Tests that task cards display the correct number of assigned members.
     */
    @Test
    fun taskCard_showsCorrectAssignedMembersCount() {
        // When
        setupHomeScreen()

        // Then@Test
        fun sortModeChanges_updatesTaskOrder() {
            // Set up tasks with distinct names and dates for clear sorting results
            val testTasksForSorting = listOf(
                Task(
                    id = "task1",
                    name = "Alpha Task", // First alphabetically
                    description = "Description",
                    creationDate = Timestamp(Timestamp.now().seconds - 7200, 0), // Oldest
                    status = TaskStatus.TODO,
                    assignedMembers = emptyList()
                ),
                Task(
                    id = "task2",
                    name = "Beta Task", // Middle alphabetically
                    description = "Description",
                    creationDate = Timestamp(Timestamp.now().seconds - 3600, 0), // Middle age
                    status = TaskStatus.IN_PROGRESS,
                    assignedMembers = emptyList()
                ),
                Task(
                    id = "task3",
                    name = "Zeta Task", // Last alphabetically
                    description = "Description",
                    creationDate = Timestamp.now(), // Newest
                    status = TaskStatus.DONE,
                    assignedMembers = emptyList()
                )
            )

            // Create mutable state flows for sort mode and tasks
            val sortModeFlow = MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)
            val sortedTasksFlow = MutableStateFlow(testTasksForSorting.sortedByDescending { it.creationDate })

            every { mockViewModel.taskSortMode } returns sortModeFlow
            every { mockViewModel.sortedTasks } returns sortedTasksFlow

            // Mock toggleSortMode to update both mode and task order
            every { mockViewModel.toggleSortMode() } answers {
                when (sortModeFlow.value) {
                    HomeViewModel.TaskSortMode.DATE_DESC -> {
                        sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_ASC
                        sortedTasksFlow.value = testTasksForSorting.sortedBy { it.name }
                    }

                    HomeViewModel.TaskSortMode.NAME_ASC -> {
                        sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_DESC
                        sortedTasksFlow.value = testTasksForSorting.sortedByDescending { it.name }
                    }

                    HomeViewModel.TaskSortMode.NAME_DESC -> {
                        sortModeFlow.value = HomeViewModel.TaskSortMode.DATE_DESC
                        sortedTasksFlow.value = testTasksForSorting.sortedByDescending { it.creationDate }
                    }
                }
            }

            // Set up the HomeScreen
            setupHomeScreen()

            // Assert initial DATE_DESC order: Zeta (newest), Beta, Alpha (oldest)
            composeTestRule.onNodeWithText("Zeta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
            composeTestRule.onNodeWithText("Beta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Alpha Task"))

            // Click sort button to change to NAME_ASC mode
            composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
            composeTestRule.waitForIdle()
            composeTestRule.mainClock.advanceTimeBy(300)

            // Assert NAME_ASC order: Alpha, Beta, Zeta
            composeTestRule.onNodeWithText("Alpha Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
            composeTestRule.onNodeWithText("Beta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Zeta Task"))

            // Click sort button to change to NAME_DESC mode
            composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
            composeTestRule.waitForIdle()
            composeTestRule.mainClock.advanceTimeBy(300)

            // Assert NAME_DESC order: Zeta, Beta, Alpha
            composeTestRule.onNodeWithText("Zeta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
            composeTestRule.onNodeWithText("Beta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Alpha Task"))

            // Click sort button to return to DATE_DESC mode
            composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
            composeTestRule.waitForIdle()
            composeTestRule.mainClock.advanceTimeBy(300)

            // Assert DATE_DESC order again: Zeta (newest), Beta, Alpha (oldest)
            composeTestRule.onNodeWithText("Zeta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
            composeTestRule.onNodeWithText("Beta Task")
                .assertIsAbove(composeTestRule.onNodeWithText("Alpha Task"))
        }
        composeTestRule.onNodeWithText("0 assigned members").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 assigned members").assertIsDisplayed()
    }

    /**
     * Tests that when a team is selected, the selectTeam method is called with the correct index.
     */
    @Test
    fun teamSelection_callsSelectTeamWithCorrectIndex() {
        // When
        setupHomeScreen()

        // Then - verify selectTeam is called when the screen is composed
        verify { mockViewModel.selectTeam(any()) }
    }

    /**
     * Tests that team cards display the correct member count text.
     */
    @Test
    fun teamCard_showsCorrectMemberCount() {
        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("1 members").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 members").assertIsDisplayed()
    }

    /**
     * Tests that section headers are displayed correctly.
     */
    @Test
    fun sectionHeaders_areDisplayedCorrectly() {
        // When
        setupHomeScreen()

        // Then
        composeTestRule.onNodeWithText("My Teams").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tasks").assertIsDisplayed()
    }

    /**
     * Tests that swiping the team pager updates the selected team index.
     */
    @Test
    fun teamPager_swipeUpdatesSelectedTeam() {
        // Given
        val selectedTeamIndexFlow = MutableStateFlow(0)
        every { mockViewModel.selectedTeamIndex } returns selectedTeamIndexFlow

        // When
        setupHomeScreen()

        // Then - verify first team is shown
        composeTestRule.onNodeWithText("Team Alpha").assertIsDisplayed()

        // When - simulate a swipe left to show the next team
        composeTestRule.onNodeWithText("Team Alpha").performTouchInput {
            swipeLeft(
                startX = centerX + 200f,
                endX = centerX - 200f,
                durationMillis = 200
            )
        }

        // Allow time for animations and recomposition
        composeTestRule.mainClock.advanceTimeBy(500)

        // Then - verify second team is shown and selectTeam was called with index 1
        composeTestRule.onNodeWithText("Team Beta").assertIsDisplayed()
        verify { mockViewModel.selectTeam(1) }
    }

    /**
     * Tests that the pager displays the correct team when selectedTeamIndex changes.
     */
    @Test
    fun teamPager_reflectsSelectedTeamIndex() {
        // Create a mutable state flow that we can update
        val selectedTeamIndexFlow = MutableStateFlow(0)
        every { mockViewModel.selectedTeamIndex } returns selectedTeamIndexFlow

        // Set up the HomeScreen with first team selected
        setupHomeScreen()
        composeTestRule.onNodeWithText("Team Alpha").assertIsDisplayed()

        // Change the selected team index
        selectedTeamIndexFlow.value = 1
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(500)

        // Verify the second team is now displayed
        composeTestRule.onNodeWithText("Team Beta").assertIsDisplayed()
    }

    /**
     * Tests that the pager displays the correct team's tasks when selectedTeamIndex changes.
     */
    @Test
    fun teamPager_reflectsSelectedTeamTasks() {
        // Create a mutable state flow that we can update
        val selectedTeamIndexFlow = MutableStateFlow(0)
        every { mockViewModel.selectedTeamIndex } returns selectedTeamIndexFlow

        // Set up different tasks for different team indices
        val team1Tasks = listOf(
            Task(
                id = "task1",
                name = "Team 1 Task",
                description = "Task for team 1",
                creationDate = Timestamp.now(),
                status = TaskStatus.TODO,
                assignedMembers = emptyList()
            )
        )

        val team2Tasks = listOf(
            Task(
                id = "task2",
                name = "Team 2 Task",
                description = "Task for team 2",
                creationDate = Timestamp.now(),
                status = TaskStatus.IN_PROGRESS,
                assignedMembers = emptyList()
            )
        )

        // Use a mutable flow for sortedTasks that we can update
        val sortedTasksFlow = MutableStateFlow(team1Tasks)
        every { mockViewModel.sortedTasks } returns sortedTasksFlow

        // Set up the HomeScreen with first team selected
        setupHomeScreen()

        // Verify first team's task is shown
        composeTestRule.onNodeWithText("Team 1 Task").assertIsDisplayed()
        composeTestRule.onNodeWithText("Team 2 Task").assertDoesNotExist()

        // Change the selected team index and update tasks
        selectedTeamIndexFlow.value = 1
        sortedTasksFlow.value = team2Tasks

        // Allow time for recomposition
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(500)

        // Verify second team's task is now displayed
        composeTestRule.onNodeWithText("Team 1 Task").assertDoesNotExist()
        composeTestRule.onNodeWithText("Team 2 Task").assertIsDisplayed()
    }

    /**
     * Tests that tasks shown update correctly when the team changes due to swiping in the horizontal pager.
     */
    @Test
    fun teamPager_swipeChangesDisplayedTasks() {
        // Given - create different tasks for different teams
        val team1Tasks = listOf(
            Task(
                id = "task1",
                name = "Team Alpha Task",
                description = "Task for Team Alpha",
                creationDate = Timestamp.now(),
                status = TaskStatus.TODO,
                assignedMembers = emptyList()
            )
        )

        val team2Tasks = listOf(
            Task(
                id = "task2",
                name = "Team Beta Task",
                description = "Task for Team Beta",
                creationDate = Timestamp.now(),
                status = TaskStatus.IN_PROGRESS,
                assignedMembers = emptyList()
            )
        )

        // Set up mutable flows for team index and tasks
        val selectedTeamIndexFlow = MutableStateFlow(0)
        val sortedTasksFlow = MutableStateFlow(team1Tasks)

        every { mockViewModel.selectedTeamIndex } returns selectedTeamIndexFlow
        every { mockViewModel.sortedTasks } returns sortedTasksFlow

        // When - set up the home screen with the first team selected
        setupHomeScreen()

        // Then - verify the first team's task is displayed
        composeTestRule.onNodeWithText("Team Alpha Task").assertIsDisplayed()
        composeTestRule.onNodeWithText("Team Beta Task").assertDoesNotExist()

        // When - simulate a swipe to change to the next team
        composeTestRule.onNodeWithText("Team Alpha").performTouchInput {
            swipeLeft(
                startX = centerX + 200f,
                endX = centerX - 200f,
                durationMillis = 200
            )
        }

        // Then - verify that selectTeam(1) was called as a result of the swipe
        verify { mockViewModel.selectTeam(1) }

        // When - update the task list to reflect what the ViewModel would do
        selectedTeamIndexFlow.value = 1
        sortedTasksFlow.value = team2Tasks

        // Give time for recomposition
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.waitForIdle()

        // Then - verify the second team's task is now displayed
        composeTestRule.onNodeWithText("Team Alpha Task").assertDoesNotExist()
        composeTestRule.onNodeWithText("Team Beta Task").assertIsDisplayed()
    }

    /**
     * Test that verifies the correct ordering of tasks when the sorting mode changes.
     */
    @Test
    fun sortModeChanges_updatesTaskOrder() {
        // Set up tasks with distinct names and dates for clear sorting results
        val testTasksForSorting = listOf(
            Task(
                id = "task1",
                name = "Alpha Task", // First alphabetically
                description = "Description",
                creationDate = Timestamp(Timestamp.now().seconds - 7200, 0), // Oldest
                status = TaskStatus.TODO,
                assignedMembers = emptyList()
            ),
            Task(
                id = "task2",
                name = "Beta Task", // Middle alphabetically
                description = "Description",
                creationDate = Timestamp(Timestamp.now().seconds - 3600, 0), // Middle age
                status = TaskStatus.IN_PROGRESS,
                assignedMembers = emptyList()
            ),
            Task(
                id = "task3",
                name = "Zeta Task", // Last alphabetically
                description = "Description",
                creationDate = Timestamp.now(), // Newest
                status = TaskStatus.DONE,
                assignedMembers = emptyList()
            )
        )

        // Create mutable state flows for sort mode and tasks
        val sortModeFlow = MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)
        val sortedTasksFlow = MutableStateFlow(testTasksForSorting.sortedByDescending { it.creationDate })

        every { mockViewModel.taskSortMode } returns sortModeFlow
        every { mockViewModel.sortedTasks } returns sortedTasksFlow

        // Mock toggleSortMode to update both mode and task order
        every { mockViewModel.toggleSortMode() } answers {
            when (sortModeFlow.value) {
                HomeViewModel.TaskSortMode.DATE_DESC -> {
                    sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_ASC
                    sortedTasksFlow.value = testTasksForSorting.sortedBy { it.name }
                }

                HomeViewModel.TaskSortMode.NAME_ASC -> {
                    sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_DESC
                    sortedTasksFlow.value = testTasksForSorting.sortedByDescending { it.name }
                }

                HomeViewModel.TaskSortMode.NAME_DESC -> {
                    sortModeFlow.value = HomeViewModel.TaskSortMode.DATE_DESC
                    sortedTasksFlow.value = testTasksForSorting.sortedByDescending { it.creationDate }
                }
            }
        }

        // Set up the HomeScreen
        setupHomeScreen()

        // Assert initial DATE_DESC order: Zeta (newest), Beta, Alpha (oldest)
        composeTestRule.onNodeWithText("Zeta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
        composeTestRule.onNodeWithText("Beta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Alpha Task"))

        // Click sort button to change to NAME_ASC mode
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(300)

        // Assert NAME_ASC order: Alpha, Beta, Zeta
        composeTestRule.onNodeWithText("Alpha Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
        composeTestRule.onNodeWithText("Beta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Zeta Task"))

        // Click sort button to change to NAME_DESC mode
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(300)

        // Assert NAME_DESC order: Zeta, Beta, Alpha
        composeTestRule.onNodeWithText("Zeta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
        composeTestRule.onNodeWithText("Beta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Alpha Task"))

        // Click sort button to return to DATE_DESC mode
        composeTestRule.onNodeWithContentDescription("Sort tasks").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(300)

        // Assert DATE_DESC order again: Zeta (newest), Beta, Alpha (oldest)
        composeTestRule.onNodeWithText("Zeta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Beta Task"))
        composeTestRule.onNodeWithText("Beta Task")
            .assertIsAbove(composeTestRule.onNodeWithText("Alpha Task"))
    }
}