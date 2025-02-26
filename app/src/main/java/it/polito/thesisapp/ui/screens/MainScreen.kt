package it.polito.thesisapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.thesisapp.navigation.Screen
import it.polito.thesisapp.ui.components.AppBottomBar
import it.polito.thesisapp.utils.FirestoreConstants

/**
 * Composable function that displays the main screen of the application.
 * It sets up the navigation host and the bottom navigation bar.
 */
@Preview
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.HOME_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.HOME_ROUTE) { HomeScreen(FirestoreConstants.UserID.USERID) }
        }
    }
}