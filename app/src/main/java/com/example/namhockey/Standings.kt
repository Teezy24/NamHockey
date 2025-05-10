package com.example.namhockey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen() {
    var selectedFormat by remember { mutableStateOf("Indoor") }
    var selectedGender by remember { mutableStateOf("Men") }
    var selectedLeague by remember { mutableStateOf("2021 Bank Windhoek Premier League") }
    
    var formatExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var leagueExpanded by remember { mutableStateOf(false) }

    // Generate leagues based on format and gender
    val leagues = when {
        selectedFormat == "Indoor" && selectedGender == "Men" ->
            listOf("2021 Bank Windhoek Premier League", "2022 Bank Windhoek Premier League")
        selectedFormat == "Indoor" && selectedGender == "Women" ->
            listOf("2021 Bank Windhoek Women's League", "2022 Bank Windhoek Women's League")
        selectedFormat == "Outdoor" && selectedGender == "Men" ->
            listOf("2021 Outdoor Premier League", "2022 Outdoor Premier League")
        else ->
            listOf("2021 Outdoor Women's League", "2022 Outdoor Women's League")
    }

    // Mock data for standings
    val standingsData = remember(selectedFormat, selectedGender, selectedLeague) {
        if (selectedFormat == "Indoor" && selectedGender == "Men" &&
            selectedLeague == "2021 Bank Windhoek Premier League") {
            // Data from the image
            listOf(
                TeamStanding(1, "Saints", 6, 6, 0, 0, 18, 6, 79, 16, 63, 24),
                TeamStanding(2, "WOBSC", 6, 5, 1, 0, 15, 5, 52, 24, 28, 20),
                TeamStanding(3, "DTS", 6, 3, 2, 1, 10, 3, 39, 30, 9, 13),
                TeamStanding(4, "SEHC", 6, 3, 3, 0, 9, 2, 25, 46, -21, 11),
                TeamStanding(5, "NUST", 6, 2, 4, 0, 6, 0, 9, 26, -17, 6),
                TeamStanding(6, "West Coast Wolves", 6, 1, 5, 0, 3, 0, 9, 46, -37, 3),
                TeamStanding(7, "Wanderers", 6, 0, 5, 1, 1, 0, 8, 33, -25, 1)
            )
        } else {
            // Generate some mock data for other combinations
            listOf(
                TeamStanding(1, "Team A", 6, 5, 1, 0, 15, 4, 35, 10, 25, 19),
                TeamStanding(2, "Team B", 6, 4, 1, 1, 13, 3, 28, 15, 13, 16),
                TeamStanding(3, "Team C", 6, 3, 2, 1, 10, 2, 22, 18, 4, 12),
                TeamStanding(4, "Team D", 6, 2, 3, 1, 7, 1, 18, 20, -2, 8),
                TeamStanding(5, "Team E", 6, 1, 4, 1, 4, 0, 12, 25, -13, 4),
                TeamStanding(6, "Team F", 6, 0, 4, 2, 2, 0, 8, 35, -27, 2)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Title Section
        Text(
            "Standings",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        HorizontalDivider(color = Color.Black, thickness = 1.dp)

        // Dropdowns Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Format Dropdown
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = formatExpanded,
                    onExpandedChange = { formatExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedFormat,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formatExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = formatExpanded,
                        onDismissRequest = { formatExpanded = false }
                    ) {
                        listOf("Indoor", "Outdoor").forEach { format ->
                            DropdownMenuItem(
                                text = { Text(format) },
                                onClick = {
                                    selectedFormat = format
                                    formatExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Gender Dropdown
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedGender,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        listOf("Men", "Women").forEach { gender ->
                            DropdownMenuItem(
                                text = { Text(gender) },
                                onClick = {
                                    selectedGender = gender
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // League Dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = leagueExpanded,
                onExpandedChange = { leagueExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedLeague,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = leagueExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = leagueExpanded,
                    onDismissRequest = { leagueExpanded = false }
                ) {
                    leagues.forEach { league ->
                        DropdownMenuItem(
                            text = { Text(league) },
                            onClick = {
                                selectedLeague = league
                                leagueExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // League Title
        Text(
            "$selectedLeague - $selectedGender",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Standings Table
        StandingsTable(standingsData)
    }
}

@Composable
fun StandingsTable(standings: List<TeamStanding>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Pos",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
            Text(
                "Team",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "P",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
            Text(
                "W",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
            Text(
                "L",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
            Text(
                "D",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
            Text(
                "Pts",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
        }

        // Table Rows
        LazyColumn {
            items(standings) { standing ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (standing.position % 2 == 0) Color(0xFFF8F8F8) else Color.White)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        standing.position.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(30.dp)
                    )
                    Text(
                        standing.teamName,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        standing.gamesPlayed.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(30.dp)
                    )
                    Text(
                        standing.wins.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(30.dp)
                    )
                    Text(
                        standing.lost.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(30.dp)
                    )
                    Text(
                        standing.draw.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(30.dp)
                    )
                    Text(
                        standing.points.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(30.dp)
                    )
                }
            }
        }
    }
}

// Data class for team standings
data class TeamStanding(
    val position: Int,
    val teamName: String,
    val gamesPlayed: Int,
    val wins: Int,
    val lost: Int,
    val draw: Int,
    val points: Int,
    val bp: Int, // Bonus points
    val gf: Int, // Goals for
    val ga: Int, // Goals against
    val gd: Int, // Goal difference
    val tPoints: Int // Total points
)