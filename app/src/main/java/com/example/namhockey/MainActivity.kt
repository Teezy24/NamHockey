package com.example.namhockey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.namhockey.ui.theme.NamHockeyTheme
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableIntStateOf
import com.example.namhockey.ui.screens.SquadScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NamHockeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding)) // Added missing modifier parameter
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) { // Added modifier parameter with default value
    val tabs = listOf("Home", "Standings", "Squad", "Settings")
    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        modifier = modifier // Pass the modifier here
    ) { innerPadding ->
        // Main content goes here
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> StandingsScreen()
                2 -> SquadScreen()
                3 -> Text("Settings Screen")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = { Text(tab) },
                icon = {
                    when (index) {
                        0 -> Icon(Icons.Default.Home, contentDescription = null)
                        1 -> Icon(Icons.Default.Menu, contentDescription = null)
                        2 -> Icon(Icons.Default.Create, contentDescription = null)
                        3 -> Icon(Icons.Default.Settings, contentDescription = null)
                        else -> Icon(Icons.Default.Home, contentDescription = null)
                    }
                }
            ) // Corrected missing closing parenthesis
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NamHockey() {
    NamHockeyTheme {
        MainScreen()
        StandingsScreen()
        SquadScreen()
    }
}
