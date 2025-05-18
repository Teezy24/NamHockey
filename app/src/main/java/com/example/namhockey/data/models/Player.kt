package com.example.namhockey.data.models

data class PlayerStats(
    val goals: Int,
    val assists: Int,
    val gamesPlayed: Int
)

data class Player(
    val id: String,
    val name: String,
    val teamId: String,
    val number: Int,
    val position: String,
    val dateOfBirth: String,
    val nationality: String,
    val height: Int,
    val weight: Int,
    val stats: PlayerStats
) 