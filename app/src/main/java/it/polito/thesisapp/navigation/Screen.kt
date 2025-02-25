package it.polito.thesisapp.navigation

import it.polito.thesisapp.R

sealed class Screen(val route: String, val icon: Int, val label: String) {
    data object Home : Screen("home", R.drawable.ic_home, "Home")
    data object Detail : Screen("detail", R.drawable.ic_detail, "Detail")
    data object List : Screen("list", R.drawable.ic_list, "List")

    companion object {
        val items = listOf(Home, Detail, List)
    }
}