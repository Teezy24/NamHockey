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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.namhockey.data.TeamRepository
import com.example.namhockey.data.PlayerRepository
import com.example.namhockey.data.EventRepository
import com.example.namhockey.data.NewsRepository
import com.example.namhockey.data.Team
import com.example.namhockey.data.Player
import com.example.namhockey.data.Event
import com.example.namhockey.data.NewsItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem




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
    data class TeamUi(val name: String, val logoResId: Int)
    val allTeams = listOf(
        TeamUi("Windhoek Wasps", android.R.drawable.ic_menu_compass),
        TeamUi("Coastal Sharks", android.R.drawable.ic_menu_mylocation),
        TeamUi("Desert Eagles", android.R.drawable.ic_menu_myplaces),
        TeamUi("Northern Wolves", android.R.drawable.ic_menu_directions),
        TeamUi("Southern Stars", android.R.drawable.ic_menu_mapmode),
        TeamUi("Capital Kings", android.R.drawable.ic_menu_manage),
        TeamUi("River Raptors", android.R.drawable.ic_menu_gallery),
        TeamUi("Savannah Lions", android.R.drawable.ic_menu_camera)
    )
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTeam by remember { mutableStateOf<TeamUi?>(null) }
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
                        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
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
    val tabs = listOf("Home", "Standings", "Squad", "Events", "News", "Settings")
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
                3 -> EventScreen()
                4 -> NewsScreen()
                5 -> SettingsScreen(
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

@Composable
fun SquadScreen() {
    var showAddTeam by remember { mutableStateOf(false) }
    var showAddPlayer by remember { mutableStateOf(false) }
    val teams = remember { mutableStateListOf<Team>() }
    val players = remember { mutableStateListOf<Player>() }
    // Sync with repository
    LaunchedEffect(Unit) {
        teams.clear()
        teams.addAll(TeamRepository.getTeams())
        players.clear()
        players.addAll(PlayerRepository.getPlayers())
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Teams", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { showAddTeam = true }, modifier = Modifier.padding(vertical = 8.dp)) { Text("Add Team") }
        teams.forEach { team ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(team.name)
                Button(onClick = {
                    TeamRepository.removeTeam(team.id)
                    teams.remove(team)
                }) { Text("Remove") }
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text("Players", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { showAddPlayer = true }, modifier = Modifier.padding(vertical = 8.dp)) { Text("Add Player") }
        players.forEach { player ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(player.name)
                Button(onClick = {
                    PlayerRepository.removePlayer(player.id)
                    players.remove(player)
                }) { Text("Remove") }
            }
        }
    }
    if (showAddTeam) {
        AddTeamDialog(onDismiss = { showAddTeam = false; teams.clear(); teams.addAll(TeamRepository.getTeams()) })
    }
    if (showAddPlayer) {
        AddPlayerDialog(onDismiss = { showAddPlayer = false; players.clear(); players.addAll(PlayerRepository.getPlayers()) }, teams = TeamRepository.getTeams())
    }
}

@Composable
fun AddTeamDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var league by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                TeamRepository.addTeam(
                    Team(
                        id = 0,
                        name = name,
                        contactPerson = contactPerson,
                        contactNumber = contactNumber,
                        email = email,
                        logoResId = null,
                        league = league,
                        format = format,
                        gender = gender
                    )
                )
                onDismiss()
            }, enabled = name.isNotBlank() && league.isNotBlank() && format.isNotBlank() && gender.isNotBlank()) { Text("Add") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add Team") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Team Name") })
                OutlinedTextField(value = contactPerson, onValueChange = { contactPerson = it }, label = { Text("Contact Person") })
                OutlinedTextField(value = contactNumber, onValueChange = { contactNumber = it }, label = { Text("Contact Number") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = league, onValueChange = { league = it }, label = { Text("League") })
                OutlinedTextField(value = format, onValueChange = { format = it }, label = { Text("Format (e.g. Indoor/Outdoor)") })
                OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender (e.g. Men/Women)") })

            }
        }
    )
}

@Composable
fun AddPlayerDialog(onDismiss: () -> Unit, teams: List<Team>) {
    var name by remember { mutableStateOf("") }
    var teamId by remember { mutableStateOf<Int?>(null) }
    var position by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                PlayerRepository.addPlayer(Player(0, name,
                    teamId.toString(), position, dob, email, phoneNumber))
                onDismiss()
            }, enabled = name.isNotBlank()) { Text("Add") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add Player") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Player Name") })
                DropdownMenuBox(teams = teams, selectedTeamId = teamId, onTeamSelected = { teamId = it })
                OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("Position") })
                OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") })
            }
        }
    )
}

@Composable
fun DropdownMenuBox(teams: List<Team>, selectedTeamId: Int?, onTeamSelected: (Int?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val selectedTeam = teams.find { it.id == selectedTeamId }
    Box {
        OutlinedTextField(
            value = selectedTeam?.name ?: "Select Team",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.clickable { expanded = true },
            label = { Text("Team") }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            teams.forEach { team ->
                DropdownMenuItem(
                    text = { Text(team.name) },
                    onClick = {
                        onTeamSelected(team.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EventScreen() {
    var showAddEvent by remember { mutableStateOf(false) }
    val events = remember { mutableStateListOf<Event>() }
    val teams = TeamRepository.getTeams()
    LaunchedEffect(Unit) {
        events.clear()
        events.addAll(EventRepository.getEvents())
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Events", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { showAddEvent = true }, modifier = Modifier.padding(vertical = 8.dp)) { Text("Add Event") }
        events.forEach { event ->
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(event.name, fontWeight = FontWeight.Bold)
                Text("Date: ${event.date}")
                Text("Location: ${event.location}")
                Text("Description: ${event.description}")
                Text("Teams Entered: ${event.teamIds.size}")
                Button(onClick = {
                    // Enter first team for demo
                    if (teams.isNotEmpty()) EventRepository.enterTeam(event.id, teams.first().id)
                    events.clear(); events.addAll(EventRepository.getEvents())
                }, enabled = teams.isNotEmpty()) { Text("Enter First Team") }
            }
        }
    }
    if (showAddEvent) {
        AddEventDialog(onDismiss = { showAddEvent = false; events.clear(); events.addAll(EventRepository.getEvents()) })
    }
}

@Composable
fun AddEventDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                EventRepository.addEvent(Event(0, name, date, location, description))
                onDismiss()
            }, enabled = name.isNotBlank()) { Text("Add") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add Event") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Event Name") })
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        }
    )
}

@Composable
fun NewsScreen() {
    var showAddNews by remember { mutableStateOf(false) }
    val news = remember { mutableStateListOf<NewsItem>() }
    LaunchedEffect(Unit) {
        news.clear()
        news.addAll(NewsRepository.getNews())
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("News & Updates", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { showAddNews = true }, modifier = Modifier.padding(vertical = 8.dp)) { Text("Add News") }
        news.forEach { item ->
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text(item.content)
                Text("Posted: ${item.timestamp}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    if (showAddNews) {
        AddNewsDialog(onDismiss = { showAddNews = false; news.clear(); news.addAll(NewsRepository.getNews()) })
    }
}

@Composable
fun AddNewsDialog(onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                NewsRepository.addNews(NewsItem(0, title, content, timestamp))
                onDismiss()
            }, enabled = title.isNotBlank()) { Text("Add") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add News") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Content") })
            }
        }
    )
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