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
import it.polito.thesisapp.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun horizontalPager_displaysCorrectTeam() {
        // Arrange
        val teams = listOf(
            Team(
                id = "1",
                name = "Team A",
                description = "AAA",
                members = emptyList(),
                tasks = emptyList()
            ),
            Team(
                id = "2",
                name = "Team B",
                description = "BBB",
                members = emptyList(),
                tasks = emptyList()
            ),
            Team(
                id = "3",
                name = "Team C",
                description = "CCC",
                members = emptyList(),
                tasks = emptyList()
            )
        )

        val mockViewModel = mockk<HomeViewModel>(relaxed = true)
        val teamsFlow = MutableStateFlow(teams)
        val selectedTeamIndexFlow = MutableStateFlow(0)
        val isLoadingFlow = MutableStateFlow(false)
        val profileFlow = MutableStateFlow(null)
        val sortedTasksFlow = MutableStateFlow<List<Task>>(emptyList())
        val taskSortModeFlow = MutableStateFlow(HomeViewModel.TaskSortMode.DATE_DESC)

        // Mock ViewModel state flows
        every { mockViewModel.teams } returns teamsFlow
        every { mockViewModel.selectedTeamIndex } returns selectedTeamIndexFlow
        every { mockViewModel.isLoading } returns isLoadingFlow
        every { mockViewModel.profile } returns profileFlow
        every { mockViewModel.sortedTasks } returns sortedTasksFlow
        every { mockViewModel.taskSortMode } returns taskSortModeFlow

        // Act
        composeTestRule.setContent {
            HomeScreen(
                userId = "test-user",
                viewModel = mockViewModel,
                onNavigateToTeam = {},
                onNavigateToTask = { _, _ -> }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Team A").assertIsDisplayed()

        // Swipe to next team
        composeTestRule.onNode(hasText("Team A")).performTouchInput {
            swipeLeft()
        }

        // Verify second team is displayed
        composeTestRule.onNodeWithText("Team B").assertIsDisplayed()

        // Verify select team was called
        verify { mockViewModel.selectTeam(1) }
    }

    @Test
    fun teamCard_clickNavigatesToTeam() {
        // Arrange
        val teams = listOf(
            Team(id = "1", name = "Team A", members = emptyList(), tasks = emptyList())
        )
        val mockViewModel = createMockViewModel(teams)
        var navigatedTeamId = ""

        // Act
        composeTestRule.setContent {
            HomeScreen(
                userId = "test-user",
                viewModel = mockViewModel,
                onNavigateToTeam = { teamId -> navigatedTeamId = teamId },
                onNavigateToTask = { _, _ -> }
            )
        }

        // Click on team card
        composeTestRule.onNodeWithText("Team A").performClick()

        // Assert navigation was triggered with correct ID
        assert(navigatedTeamId == "1")
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