package com.example.namhockey.data

import com.example.namhockey.R

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Repository {
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: Flow<List<Team>> = _teams.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: Flow<List<Player>> = _players.asStateFlow()

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: Flow<List<Match>> = _matches.asStateFlow()

    private val _leagues = MutableStateFlow<List<League>>(emptyList())
    val leagues: Flow<List<League>> = _leagues.asStateFlow()

    init {
        // Initialize with mock data
        initializeMockData()
    }

    private fun initializeMockData() {
        // Mock Teams
        val mockTeams = listOf(
            Team(1, "Saints", R.drawable.saintslogo, "2021 Bank Windhoek Premier League", "Indoor", "Men"),
            Team(2, "DTS", R.drawable.dtslogo, "2021 Bank Windhoek Premier League", "Indoor", "Men"),
            Team(3, "WOBSC", R.drawable.teamicon, "2021 Bank Windhoek Premier League", "Indoor", "Men"),
            Team(4, "SEHC", R.drawable.teamicon, "2021 Bank Windhoek Premier League", "Indoor", "Men")
        )
        _teams.value = mockTeams

        // Mock Players
        val mockPlayers = listOf(
            Player(1, 1, "Dr Naftali Indongo", "Forward", "Player", 5, 4.5f),
            Player(2, 1, "Dr Simon Muchininenyika", "Defense", "Player", 2, 4.2f),
            Player(3, 2, "Dr Gabirel Nhinda", "Forward", "Player", 7, 4.8f),
            Player(4, 2, "Ms Rosetha Kays", "Goalkeeper", "Player", 0, 4.6f)
        )
        _players.value = mockPlayers

        // Mock Matches
        val mockMatches = listOf(
            Match(1, 1, 2, 4, 3, "2024-03-15", MatchStatus.COMPLETED, "2021 Bank Windhoek Premier League"),
            Match(2, 3, 4, 2, 2, "2024-03-16", MatchStatus.COMPLETED, "2021 Bank Windhoek Premier League"),
            Match(3, 1, 3, 0, 0, "2024-03-20", MatchStatus.SCHEDULED, "2021 Bank Windhoek Premier League")
        )
        _matches.value = mockMatches

        // Mock Leagues
        val mockLeagues = listOf(
            League(1, "2021 Bank Windhoek Premier League", "Indoor", "Men", "2021"),
            League(2, "2021 Bank Windhoek Women's League", "Indoor", "Women", "2021"),
            League(3, "2022 Bank Windhoek Premier League", "Indoor", "Men", "2022"),
            League(4, "2022 Bank Windhoek Women's League", "Indoor", "Women", "2022")
        )
        _leagues.value = mockLeagues
    }

    fun getTeamsByLeague(league: String): List<Team> {
        return _teams.value.filter { it.league == league }
    }

    fun getPlayersByTeam(teamId: Int): List<Player> {
        return _players.value.filter { it.teamId == teamId }
    }

    fun getMatchesByLeague(league: String): List<Match> {
        return _matches.value.filter { it.league == league }
    }

    fun getLeaguesByFormatAndGender(format: String, gender: String): List<League> {
        return _leagues.value.filter { it.format == format && it.gender == gender }
    }

    fun getTeamStandings(league: String): List<TeamStanding> {
        // This would typically come from a database or API
        // For now, returning mock data
        return listOf(
            TeamStanding(1, "Saints", 6, 6, 0, 0, 18, 6, 79, 16, 63, 24),
            TeamStanding(2, "WOBSC", 6, 5, 1, 0, 15, 5, 52, 24, 28, 20),
            TeamStanding(3, "DTS", 6, 3, 2, 1, 10, 3, 39, 30, 9, 13),
            TeamStanding(4, "SEHC", 6, 3, 3, 0, 9, 2, 25, 46, -21, 11)
        )
    }
}