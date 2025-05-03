package com.example.namhockey

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.text.contains

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsListView(
    teams: List<Team>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTeamSelected: (Team) -> Unit,
    onAddTeamClicked: () -> Unit
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
                onClick = onAddTeamClicked,
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Spacer(modifier = Modifier.width(16.dp))

        // Team name
        Text(
            text = team.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
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

@Composable
fun TeamDetailView(team: Team, onBackClicked: () -> Unit) {
    // Mock data for team details
    val manager = remember { Manager("Manager", "M") }
    val players = remember {
        listOf(
            Player(1, "Player 1", "CM", "C", 5, 4.5f),
            Player(2, "Player 2", "GK", "", 0, 4.2f),
            Player(3, "Player 3", "D", "", 2, 4.0f)
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

        HorizontalDivider()

        // Team roster table
        TeamRosterTable(manager = manager, players = players)
    }
}

// Replace deprecated Divider with HorizontalDivider
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = Color.Gray
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}

@Composable
fun SquadScreen() {
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddTeamForm by remember { mutableStateOf(false) }

    // Mock data
    val teams = remember {
        listOf(
            Team(1, "Team #1"),
            Team(2, "Team #2"),
            Team(3, "Team #3"),
            Team(4, "Team #4"),
            Team(5, "Team #5")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showAddTeamForm -> {
                TeamRegistrationForm(onSubmit = { showAddTeamForm = false })
            }
            selectedTeam == null -> {
                TeamsListView(
                    teams = teams,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onTeamSelected = { selectedTeam = it },
                    onAddTeamClicked = { showAddTeamForm = true }
                )
            }
            else -> {
                TeamDetailView(
                    team = selectedTeam!!,
                    onBackClicked = { selectedTeam = null }
                )
            }
        }
    }
}

@Composable
fun TeamRegistrationForm(onSubmit: () -> Unit) {
    var clubName by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var umpireName by remember { mutableStateOf("") }
    var umpireContact by remember { mutableStateOf("") }
    var umpireEmail by remember { mutableStateOf("") }
    var technicalOfficial by remember { mutableStateOf("") }
    var technicalContact by remember { mutableStateOf("") }
    var technicalEmail by remember { mutableStateOf("") }
    var selectedLeagues = remember { mutableStateListOf<String>() }
    var consentChecked by remember { mutableStateOf(false) }

    val leagues = listOf(
        "League 1",
        "League 2",
        "League 3",
        "League 4"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Complete the form below to enter your team for a league.", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = clubName, onValueChange = { clubName = it }, label = { Text("Club Name *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contactPerson, onValueChange = { contactPerson = it }, label = { Text("Club Contact Person *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contactNumber, onValueChange = { contactNumber = it }, label = { Text("Contact Person Cell Number *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = confirmEmail, onValueChange = { confirmEmail = it }, label = { Text("Confirm Email *") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
        Text("Nominated Umpire", fontWeight = FontWeight.SemiBold)
        OutlinedTextField(value = umpireName, onValueChange = { umpireName = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = umpireContact, onValueChange = { umpireContact = it }, label = { Text("Contact Details") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = umpireEmail, onValueChange = { umpireEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
        Text("Nominated Technical Official", fontWeight = FontWeight.SemiBold)
        OutlinedTextField(value = technicalOfficial, onValueChange = { technicalOfficial = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = technicalContact, onValueChange = { technicalContact = it }, label = { Text("Contact Details") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = technicalEmail, onValueChange = { technicalEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Leagues to Participate In *", fontWeight = FontWeight.Bold)

        val leagueOptions = listOf(
            "Indoor Men Premier", "Indoor Women Premier", "Indoor Men Reserve", "Indoor Women Reserve",
            "Indoor Men First", "Indoor Women First", "Indoor Men U/16", "Indoor Women U/16",
            "Outdoor Men Premier", "Outdoor Women Premier", "Outdoor Men Reserve", "Outdoor Women Reserve",
            "Outdoor Men First", "Outdoor Women First", "Outdoor Men U/16", "Outdoor Women U/16"
        )

        leagueOptions.forEach { league ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = league in selectedLeagues,
                    onCheckedChange = {
                        if (it) selectedLeagues.add(league) else selectedLeagues.remove(league)
                    }
                )
                Text(text = league)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Disclaimer *",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            "I will ensure that any/all participants from my club have updated and paid registrations with the NHU. I understand that no member that is not up to date with either their registration nor their payment may participate in any NHU event.\n\n" +
                    "My application to the tournament will only be final once I have sent a team roster for each one of the teams the club entered to the official correspondence (secretary@namibiahockey.org) and received a response.\n\n" +
                    "I understand that my club will be held liable to know and act according to the statutes and tournament rules at all times whilst competing in any NHU tournament.\n\n" +
                    "I Understand and will comply with the terms and conditions listed above.",
            fontSize = 14.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consentChecked, onCheckedChange = { consentChecked = it })
            Text("I agree to the above terms.")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (consentChecked) {
                    // Here you can perform form validation or submission
                    onSubmit()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = consentChecked
        ) {
            Text("Submit")
        }
    }

}
