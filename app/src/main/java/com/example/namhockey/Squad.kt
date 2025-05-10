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
data class TeamUi(
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
    teams: List<TeamUi>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTeamSelected: (TeamUi) -> Unit,
    onAddTeamClicked: () -> Unit,
    onAddPlayerClicked: () -> Unit
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
                onClick = onAddPlayerClicked,
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
fun TeamListItem(team: TeamUi, onClick: () -> Unit) {
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
fun TeamDetailView(team: TeamUi, onBackClicked: () -> Unit) {
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
fun SquadScreenAdavanced() {
    var selectedTeam by remember { mutableStateOf<TeamUi?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddTeamForm by remember { mutableStateOf(false) }
    var showAddPlayerForm by remember { mutableStateOf(false) }

    // Mock data
    val teams = remember {
        listOf(
            TeamUi(1, "Team #1"),
            TeamUi(2, "Team #2"),
            TeamUi(3, "Team #3"),
            TeamUi(4, "Team #4"),
            TeamUi(5, "Team #5")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showAddTeamForm -> {
                TeamRegistrationForm(
                    onSubmit = { showAddTeamForm = false },
                    onBackClicked = { showAddTeamForm = false }
                )
            }
            showAddPlayerForm -> {
                PlayerRegistrationForm(
                    onSubmit = { showAddPlayerForm = false },
                    onBackClicked = { showAddPlayerForm = false }
                )
            }
            selectedTeam == null -> {
                TeamsListView(
                    teams = teams,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onTeamSelected = { selectedTeam = it },
                    onAddTeamClicked = { showAddTeamForm = true },
                    onAddPlayerClicked = { showAddPlayerForm = true }
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
fun TeamRegistrationForm(onSubmit: () -> Unit, onBackClicked: () -> Unit) {
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
        // Back arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Team Registration",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

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
            "League 1",
            "League 2",
            "League 3",
            "League 4"
        )

        leagueOptions.forEach { league ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedLeagues.contains(league),
                    onCheckedChange = {
                        if (it) selectedLeagues.add(league) else selectedLeagues.remove(league)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(league)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consentChecked, onCheckedChange = { consentChecked = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("I agree to the Terms & Conditions")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (consentChecked && clubName.isNotBlank() && contactPerson.isNotBlank() &&
                    contactNumber.isNotBlank() && email.isNotBlank() && confirmEmail == email
                ) {
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

@Composable
fun PlayerRegistrationForm(onSubmit: () -> Unit, onBackClicked: () -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var clubName by remember { mutableStateOf("") }
    var teamCategory by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var jerseyNumber by remember { mutableStateOf("") }
    var consentConduct by remember { mutableStateOf(false) }
    var consentMedia by remember { mutableStateOf(false) }
    var consentTerms by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Player Registration",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Personal Information", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = surname, onValueChange = { surname = it }, label = { Text("Surname *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nationality, onValueChange = { nationality = it }, label = { Text("Nationality") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Text("Contact Information", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Text("Team Information", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = clubName, onValueChange = { clubName = it }, label = { Text("Club Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = teamCategory, onValueChange = { teamCategory = it }, label = { Text("Team Category") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("Player Position") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = jerseyNumber, onValueChange = { jerseyNumber = it }, label = { Text("Jersey Number") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Text("Consent", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consentConduct, onCheckedChange = { consentConduct = it })
            Spacer(Modifier.width(8.dp))
            Text("I agree to the Code of Conduct")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consentMedia, onCheckedChange = { consentMedia = it })
            Spacer(Modifier.width(8.dp))
            Text("I consent to image/media use")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consentTerms, onCheckedChange = { consentTerms = it })
            Spacer(Modifier.width(8.dp))
            Text("I agree to Terms & Conditions")
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (consentTerms && firstName.isNotBlank() && surname.isNotBlank() && phoneNumber.isNotBlank() && email.isNotBlank()) {
                    onSubmit()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
