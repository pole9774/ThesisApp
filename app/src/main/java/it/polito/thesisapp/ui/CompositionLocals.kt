package it.polito.thesisapp.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import it.polito.thesisapp.navigation.NavigationManager

val LocalNavController =
    compositionLocalOf<NavHostController> { error("No NavController provided") }

val LocalNavigationManager =
    compositionLocalOf<NavigationManager> { error("No NavigationManager provided") }