package com.example.namhockey.data

data class Team(
    val id: Int,
    val name: String,
    val logo: String,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val points: Int = 0
)

data class Player(
    val id: Int,
    val name: String,
    val number: Int,
    val position: String,
    val teamId: Int,
    val goals: Int = 0,
    val assists: Int = 0
)

data class Standing(
    val team: Team,
    val gamesPlayed: Int,
    val points: Int,
    val rank: Int
) 