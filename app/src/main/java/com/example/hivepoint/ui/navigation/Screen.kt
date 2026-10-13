package com.example.hivepoint.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Map : Screen("map", "Harta Principală")
    object AddHive : Screen("add_hive", "Adaugă Stup Nou")
    object Settings : Screen("settings", "Setări")
    object About : Screen("about", "Despre")
}
