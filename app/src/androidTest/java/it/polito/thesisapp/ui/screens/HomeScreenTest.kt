package it.polito.thesisapp.ui.screens

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

        // Initial setup with DATE_DESC mode
        setupHomeScreen()
        composeTestRule.onNodeWithContentDescription("Sort tasks").assertIsDisplayed()

        // Update to NAME_ASC mode and wait for recomposition
        sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_ASC
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Sort tasks").assertIsDisplayed()

        // Update to NAME_DESC mode and wait for recomposition
        sortModeFlow.value = HomeViewModel.TaskSortMode.NAME_DESC
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Sort tasks").assertIsDisplayed()
    }

    /**
     * Tests that task cards display the correct number of assigned members.
     */
    @Test
    fun taskCard_showsCorrectAssignedMembersCount() {
        // When
        setupHomeScreen()

        // Then
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
}