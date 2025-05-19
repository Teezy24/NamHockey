package com.example.namhockey.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.namhockey.data.Player
import com.example.namhockey.data.Standing
import com.example.namhockey.data.Team

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to NamHockey",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your ultimate hockey companion",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun StandingsScreen() {
    // Sample data - in a real app, this would come from a ViewModel
    val standings = remember {
        listOf(
            Standing(Team(1, "Team A", ""), 10, 20, 1),
            Standing(Team(2, "Team B", ""), 10, 18, 2),
            Standing(Team(3, "Team C", ""), 10, 15, 3)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(standings) { standing ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${standing.rank}. ${standing.team.name}")
                    Text(text = "Pts: ${standing.points}")
                }
            }
        }
    }
}

@Composable
fun SquadScreen() {
    // Sample data - in a real app, this would come from a ViewModel
    val players = remember {
        listOf(
            Player(1, "John Doe", 23, "Forward", 1),
            Player(2, "Jane Smith", 45, "Defense", 1),
            Player(3, "Mike Johnson", 12, "Goalie", 1)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(players) { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = player.name)
                        Text(
                            text = "#${player.number} - ${player.position}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Goals: ${player.goals}")
                        Text(text = "Assists: ${player.assists}")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add settings options here
        Text("Settings options will be implemented here")
    }
} 