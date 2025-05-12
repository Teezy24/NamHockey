package com.example.namhockey.data

data class Player(
    val id: Int,
    val name: String,
    val teamId: String,
    val position: String,
    val dob: String,
    val email: String,
    val phoneNumber: String
    val role: String
    val goals: Int,
    val rating: Float
)

object PlayerRepository {
    private val players = mutableListOf<Player>()
    private var nextId = 1

    fun addPlayer(player: Player) {
        players.add(player.copy(id = nextId++))
    }

    fun removePlayer(playerId: Int) {
        players.removeAll { it.id == playerId }
    }

    fun getPlayers(): List<Player> = players
    fun getPlayersForTeam(teamId: String): List<Player> = players.filter { it.teamId == teamId }
} 