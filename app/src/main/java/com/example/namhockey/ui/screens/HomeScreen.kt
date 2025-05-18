package com.example.namhockey.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namhockey.data.models.*
import com.example.namhockey.ui.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val teams by viewModel.teams.collectAsState()
    val standings by viewModel.standings.collectAsState()
    val news by viewModel.news.collectAsState()
    val events by viewModel.events.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedTeam by remember { mutableStateOf<Team?>(null) }

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header
            item {
                Text(
                    text = "Nam Hockey League",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Error message if any
            error?.let {
                item {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Latest News Section
            item {
                Text(
                    text = "Latest News",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(viewModel.getLatestNews()) { newsItem ->
                NewsCard(newsItem)
            }

            // Upcoming Events Section
            item {
                Text(
                    text = "Upcoming Events",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(viewModel.getUpcomingEvents()) { event ->
                EventCard(event, teams)
            }

            // Standings Section
            item {
                Text(
                    text = "Current Standings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                StandingsTable(
                    standings = standings,
                    teams = teams,
                    onTeamClick = { selectedTeam = it }
                )
            }
        }

        // Team Details Dialog
        selectedTeam?.let { team ->
            TeamDetailsDialog(
                team = team,
                standing = viewModel.getTeamStanding(team.id),
                players = viewModel.getPlayersForTeam(team.id),
                onDismiss = { selectedTeam = null }
            )
        }
    }
}

@Composable
fun NewsCard(news: News) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = news.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = news.content)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "By ${news.author}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        .format(Date(news.timestamp)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun EventCard(event: Event, teams: List<Team>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Date: ${event.date}")
            Text(text = "Location: ${event.location}")
            Text(text = event.description)
            if (event.teamIds.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Participating Teams:",
                    fontWeight = FontWeight.Medium
                )
                event.teamIds.forEach { teamId ->
                    teams.find { it.id == teamId }?.let { team ->
                        Text("• ${team.name}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = when (event.status) {
                    "Upcoming" -> MaterialTheme.colorScheme.primary
                    "Registration Open" -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.tertiary
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = event.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun StandingsTable(
    standings: List<Standing>,
    teams: List<Team>,
    onTeamClick: (Team) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Pos", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                Text("Team", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("P", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                Text("W", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                Text("D", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                Text("L", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                Text("Pts", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Standings rows
            LazyColumn {
                items(standings.sortedBy { it.position }) { standing ->
                    val team = teams.find { it.id == standing.teamId }
                    team?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onTeamClick(team) }
                        ) {
                            Text(standing.position.toString(), modifier = Modifier.width(40.dp))
                            Text(team.name, modifier = Modifier.weight(1f))
                            Text(standing.gamesPlayed.toString(), modifier = Modifier.width(40.dp))
                            Text(standing.wins.toString(), modifier = Modifier.width(40.dp))
                            Text(standing.draws.toString(), modifier = Modifier.width(40.dp))
                            Text(standing.losses.toString(), modifier = Modifier.width(40.dp))
                            Text(standing.points.toString(), modifier = Modifier.width(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsDialog(
    team: Team,
    standing: Standing?,
    players: List<Player>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(team.name, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // Team Info
                Text("City: ${team.city}")
                Text("Founded: ${team.founded}")
                Text("Home Venue: ${team.homeVenue}")
                Text("Colors: ${team.colors.joinToString(", ")}")
                Text(team.description)
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Standing Info
                standing?.let {
                    Text("Current Position: ${it.position}")
                    Text("Points: ${it.points}")
                    Text("Record: ${it.wins}W ${it.draws}D ${it.losses}L")
                    Text("Goals: ${it.goalsFor} for, ${it.goalsAgainst} against")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Players
                Text("Players:", fontWeight = FontWeight.Bold)
                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(players) { player ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                "#${player.number} ${player.name}",
                                modifier = Modifier.weight(1f)
                            )
                            Text(player.position)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
} 