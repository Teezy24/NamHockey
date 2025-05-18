package com.example.namhockey.data.models

data class Standing(
    val teamId: String,
    val position: Int,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int
)

data class StandingsData(
    val standings: List<Standing>,
    val lastUpdated: String
) 