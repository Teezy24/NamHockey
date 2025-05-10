package com.example.namhockey.data

data class Team(
    val id: Int,
    val name: String,
    val contactPerson: String,
    val contactNumber: String,
    val email: String,
    val logoResId: Int? = null
)

object TeamRepository {
    private val teams = mutableListOf<Team>()
    private var nextId = 1

    fun addTeam(team: Team) {
        teams.add(team.copy(id = nextId++))
    }

    fun removeTeam(teamId: Int) {
        teams.removeAll { it.id == teamId }
    }

    fun getTeams(): List<Team> = teams
} 