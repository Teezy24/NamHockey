package com.example.namhockey.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namhockey.R

// Mock data classes
data class Team(
    val id: Int,
    val name: String,
    val logoResId: Int = R.drawable.teamicon // Default placeholder
)

data class Player(
    val id: Int,
    val name: String,
    val position: String,
    val role: String,
    val goals: Int,
    val rating: Float
)

data class Manager(
    val name: String,
    val role: String
)

@Composable
fun SquadScreen() {
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Mock data
    val teams = remember {
        listOf(
            Team(1, "Team #1"),
            Team(2, "Team #2"),
            Team(3, "Team #3"),
            Team(4, "Team #4")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (selectedTeam == null) {
            // Teams list view
            TeamsListView(
                teams = teams,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onTeamSelected = { selectedTeam = it }
            )
        } else {
            // Team detail view
            TeamDetailView(
                team = selectedTeam!!,
                onBackClicked = { selectedTeam = null }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsListView(
    teams: List<Team>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTeamSelected: (Team) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Squad",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Add team functionality */ },
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Team")
                }
            }

            Button(
                onClick = { /* Add player functionality */ },
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Player")
                }
            }
        }

        // Teams list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(teams.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }) { team ->
                TeamListItem(team = team, onClick = { onTeamSelected(team) })
            }
        }
    }
}

@Composable
fun TeamListItem(team: Team, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = team.name,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun TeamDetailView(team: Team, onBackClicked: () -> Unit) {
    // Mock data for team details
    val manager = remember { Manager("Manager", "M") }
    val players = remember {
        listOf(
            Player(1, "PLAYER 1", "CM", "C", 5, 4.5f),
            Player(2, "PLAYER 2", "GK", "", 0, 4.2f),
            Player(3, "PLAYER 3", "D", "", 2, 4.0f)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = team.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Team logo placeholder
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(4.dp),
                color = Color.LightGray
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Logo", fontSize = 12.sp)
                }
            }
        }

        Divider()

        // Team roster table
        TeamRosterTable(manager = manager, players = players)
    }
}

@Composable
fun TeamRosterTable(manager: Manager, players: List<Player>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Table header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "POS",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Role",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Goals",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Rating",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )
        }

        Divider()

        // Manager row with gray background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "",
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Manager",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Text(
                text = manager.role,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "",
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "",
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )
        }

        Divider()

        // Player rows
        players.forEachIndexed { index, player ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (index % 2 == 0) Color.LightGray.copy(alpha = 0.1f) else Color.White)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = player.position,
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = player.name,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = player.role,
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = player.goals.toString(),
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = player.rating.toString(),
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center
                )
            }

            Divider()
        }
    }
}