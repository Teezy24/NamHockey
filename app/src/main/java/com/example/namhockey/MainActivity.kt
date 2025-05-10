package com.example.namhockey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namhockey.ui.theme.NamHockeyTheme
import androidx.compose.material.icons.filled.Settings
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkMode by remember { mutableStateOf(false) }
            var notificationsEnabled by remember { mutableStateOf(true) }
            var isLoggedIn by remember { mutableStateOf(UserRepository.isLoggedIn(this)) }
            var favouriteTeam by remember { mutableStateOf(UserRepository.getFavouriteTeam(this)) }
            NamHockeyTheme(darkTheme = darkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when {
                        !isLoggedIn -> {
                            LoginScreen(
                                context = this,
                                onLoginSuccess = { isLoggedIn = true }
                            )
                        }
                        favouriteTeam == null -> {
                            FavouriteTeamPickerScreen(
                                onTeamSelected = { team ->
                                    UserRepository.setFavouriteTeam(this, team)
                                    favouriteTeam = team
                                }
                            )
                        }
                        else -> {
                            MainScreen(
                                modifier = Modifier.padding(innerPadding),
                                darkMode = darkMode,
                                onDarkModeChanged = { darkMode = it },
                                notificationsEnabled = notificationsEnabled,
                                onNotificationsEnabledChanged = { notificationsEnabled = it },
                                onLogout = {
                                    UserRepository.logout(this)
                                    isLoggedIn = false
                                    UserRepository.clearFavouriteTeam(this)
                                    favouriteTeam = null
                                },
                                favouriteTeam = favouriteTeam
                            )
                        }
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
            Text(text = "Login", modifier = Modifier.padding(bottom = 16f))
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
                modifier = Modifier.padding(top = 16f)
            ) {
                Text("Login")
            }
        }
    }
}

@Composable
fun FavouriteTeamPickerScreen(onTeamSelected: (String) -> Unit) {
    val teams = listOf(
        "Namibia Eagles" to R.drawable.teamicon,
        "Windhoek Warriors" to R.drawable.saintslogo,
        "Coastal Sharks" to R.drawable.dtslogo
    )
    var selected by remember { mutableStateOf<String?>(null) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Choose Your Favourite Team", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            teams.forEach { (name, logoRes) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(8.dp)
                        .clickable {
                            selected = name
                            onTeamSelected(name)
                        }
                        .background(
                            if (selected == name) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = logoRes),
                        contentDescription = "$name logo",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(name, fontSize = 20.sp)
                }
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
    favouriteTeam: String? = null
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
                0 -> HomeScreen(favouriteTeam)
                1 -> StandingsScreen()
                2 -> SquadScreen()
                3 -> SettingsScreen(
                    darkMode = darkMode,
                    onDarkModeChanged = onDarkModeChanged,
                    notificationsEnabled = notificationsEnabled,
                    onNotificationsEnabledChanged = onNotificationsEnabled,
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
fun HomeScreen(favouriteTeam: String?) {
    val teamLogos = mapOf(
        "Namibia Eagles" to R.drawable.teamicon,
        "Windhoek Warriors" to R.drawable.saintslogo,
        "Coastal Sharks" to R.drawable.dtslogo
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        if (favouriteTeam != null) {
            Icon(
                painter = painterResource(id = teamLogos[favouriteTeam] ?: R.drawable.ic_launcher_foreground),
                contentDescription = "$favouriteTeam logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Welcome, $favouriteTeam fan!", style = MaterialTheme.typography.headlineMedium)
        } else {
            Text("Welcome to NamHockey!", style = MaterialTheme.typography.headlineMedium)
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
        // You may want to remove StandingsScreen() and SquadScreen() here,
        // as MainScreen already handles which screen to show.
    }
}