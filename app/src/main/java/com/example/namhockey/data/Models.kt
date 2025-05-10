package com.example.namhockey.data

data class TeamModel(
    val id: Int,
    val name: String,
    val logoResId: Int,
    val league: String,
    val format: String,
    val gender: String
)

data class Player(
    val id: Int,
    val teamId: Int,
    val name: String,
    val position: String,
    val role: String,
    val goals: Int,
    val rating: Float
)

data class Match(
    val id: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeScore: Int,
    val awayScore: Int,
    val date: String,
    val status: MatchStatus,
    val league: String
)

enum class MatchStatus {
    SCHEDULED,
    LIVE,
    COMPLETED
}

data class League(
    val id: Int,
    val name: String,
    val format: String,
    val gender: String,
    val season: String
)

data class TeamStanding(
    val position: Int,
    val teamName: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val points: Int,
    val bonusPoints: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val totalPoints: Int
)

data class News(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: Long
) 