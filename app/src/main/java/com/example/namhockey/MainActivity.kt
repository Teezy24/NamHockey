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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.namhockey.navigation.AppNavigation
import com.example.namhockey.navigation.Screen
import com.example.namhockey.ui.theme.NamHockeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NamHockeyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            tabs = listOf("Home", "Standings", "Squad", "Settings"),
                            selectedTab = 0,
                            onTabSelected = { /* Navigation will be handled by NavController */ }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AppNavigation()
                    }
                }
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
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NamHockeyPreview() {
    NamHockeyTheme {
        AppNavigation()
    }
}
