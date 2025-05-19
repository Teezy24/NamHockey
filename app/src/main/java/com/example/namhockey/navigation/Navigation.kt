package com.example.namhockey.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.namhockey.screens.HomeScreen
import com.example.namhockey.screens.StandingsScreen
import com.example.namhockey.screens.SquadScreen
import com.example.namhockey.screens.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Standings : Screen("standings")
    object Squad : Screen("squad")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Standings.route) { StandingsScreen() }
        composable(Screen.Squad.route) { SquadScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
} 