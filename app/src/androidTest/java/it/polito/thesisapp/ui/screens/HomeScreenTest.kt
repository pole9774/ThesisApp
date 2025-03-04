package it.polito.thesisapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import it.polito.thesisapp.model.Task
import it.polito.thesisapp.model.Team
import it.polito.thesisapp.model.TeamMember
import it.polito.thesisapp.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
}