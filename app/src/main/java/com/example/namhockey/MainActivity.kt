package com.example.namhockey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.namhockey.ui.theme.NamHockeyTheme
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import kotlinx.coroutines.launch
import com.example.namhockey.data.UserRepository
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkMode by remember { mutableStateOf(false) }
            var notificationsEnabled by remember { mutableStateOf(true) }
            var isLoggedIn by remember { mutableStateOf(UserRepository.isLoggedIn(this)) }
            var favoriteTeam by remember { mutableStateOf(UserRepository.getFavoriteTeam(this)) }
            NamHockeyTheme(darkTheme = darkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (!isLoggedIn) {
                        LoginScreen(
                            context = this,
                            onLoginSuccess = { isLoggedIn = true }
                        )
                    } else if (favoriteTeam == null) {
                        FavoriteTeamScreen(
                            context = this,
                            onTeamSelected = { team ->
                                UserRepository.setFavoriteTeam(this, team)
                                favoriteTeam = team
                            }
                        )
                    } else {
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            darkMode = darkMode,
                            onDarkModeChanged = { darkMode = it },
                            notificationsEnabled = notificationsEnabled,
                            onNotificationsEnabledChanged = { notificationsEnabled = it },
                            onLogout = {
                                UserRepository.logout(this)
                                isLoggedIn = false
                                favoriteTeam = null
                            },
                            favoriteTeam = favoriteTeam
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(context: Context, onLoginSuccess: () -> Unit) {
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Login", modifier = Modifier.padding(bottom = 16.dp))
            androidx.compose.material3.OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            androidx.compose.material3.OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
            )
            if (error != null) {
                Text(text = error!!, color = androidx.compose.ui.graphics.Color.Red)
            }
            androidx.compose.material3.Button(
                onClick = {
                    scope.launch {
                        val success = UserRepository.login(context, username, password)
                        if (success) {
                            error = null
                            onLoginSuccess()
                        } else {
                            error = "Invalid credentials."
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Login")
            }
        }
    }
}

@Composable
fun FavoriteTeamScreen(context: Context, onTeamSelected: (String) -> Unit) {
    data class Team(val name: String, val logoResId: Int)
    val allTeams = listOf(
        Team("Windhoek Wasps", android.R.drawable.ic_menu_compass),
        Team("Coastal Sharks", android.R.drawable.ic_menu_mylocation),
        Team("Desert Eagles", android.R.drawable.ic_menu_myplaces),
        Team("Northern Wolves", android.R.drawable.ic_menu_directions),
        Team("Southern Stars", android.R.drawable.ic_menu_mapmode),
        Team("Capital Kings", android.R.drawable.ic_menu_manage),
        Team("River Raptors", android.R.drawable.ic_menu_gallery),
        Team("Savannah Lions", android.R.drawable.ic_menu_camera)
    )
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    val teams = allTeams.filter { it.name.contains(searchQuery.text, ignoreCase = true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                "Choose Your Favourite Team",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search teams...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(teams) { team ->
                    val isSelected = selectedTeam == team
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = if (isSelected) 8.dp else 2.dp,
                        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTeam = team }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = team.logoResId),
                                contentDescription = team.name,
                                modifier = Modifier
                                    .size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                team.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { selectedTeam?.let { onTeamSelected(it.name) } },
                enabled = selectedTeam != null,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Confirm", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsEnabledChanged: (Boolean) -> Unit,
    onLogout: (() -> Unit)? = null,
    favoriteTeam: String? = null
) {
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
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeScreen(favoriteTeam = favoriteTeam)
                1 -> StandingsScreen()
                2 -> SquadScreen()
                3 -> SettingsScreen(
                    darkMode = darkMode,
                    onDarkModeChanged = onDarkModeChanged,
                    notificationsEnabled = notificationsEnabled,
                    onNotificationsEnabledChanged = onNotificationsEnabledChanged,
                    onLogout = onLogout
                )
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

@Composable
fun HomeScreen(favoriteTeam: String?) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (favoriteTeam != null) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Favourite Team: $favoriteTeam",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Welcome to NamHockey!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NamHockey() {
    NamHockeyTheme {
        MainScreen(
            darkMode = false,
            onDarkModeChanged = {},
            notificationsEnabled = true,
            onNotificationsEnabledChanged = {}
        )

    }
}