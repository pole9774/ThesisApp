package it.polito.thesisapp.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import it.polito.thesisapp.navigation.NavigationManager

/**
 * CompositionLocal to provide a NavHostController.
 * Throws an error if no NavController is provided.
 */
val LocalNavController =
    compositionLocalOf<NavHostController> { error("No NavController provided") }

/**
 * CompositionLocal to provide a NavigationManager.
 * Throws an error if no NavigationManager is provided.
 */
val LocalNavigationManager =
    compositionLocalOf<NavigationManager> { error("No NavigationManager provided") }