package com.example.namhockey.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.namhockey.data.ResourceRepository
import com.example.namhockey.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ResourceRepository.getInstance(application)

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _standings = MutableStateFlow<List<Standing>>(emptyList())
    val standings: StateFlow<List<Standing>> = _standings

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load teams
            repository.getTeams()
                .catch { e -> _error.value = "Error loading teams: ${e.message}" }
                .collect { _teams.value = it }

            // Load players
            repository.getPlayers()
                .catch { e -> _error.value = "Error loading players: ${e.message}" }
                .collect { _players.value = it }

            // Load standings
            repository.getStandings()
                .catch { e -> _error.value = "Error loading standings: ${e.message}" }
                .collect { _standings.value = it.standings }

            // Load events
            repository.getEvents()
                .catch { e -> _error.value = "Error loading events: ${e.message}" }
                .collect { _events.value = it }

            // Load news
            repository.getNews()
                .catch { e -> _error.value = "Error loading news: ${e.message}" }
                .collect { _news.value = it }
        }
    }

    fun getTeamById(teamId: String): Team? {
        return teams.value.find { it.id == teamId }
    }

    fun getPlayersForTeam(teamId: String): List<Player> {
        return players.value.filter { it.teamId == teamId }
    }

    fun getTeamStanding(teamId: String): Standing? {
        return standings.value.find { it.teamId == teamId }
    }

    fun getUpcomingEvents(limit: Int = 3): List<Event> {
        return events.value
            .filter { it.status == "Upcoming" }
            .sortedBy { it.date }
            .take(limit)
    }

    fun getLatestNews(limit: Int = 5): List<News> {
        return news.value
            .sortedByDescending { it.timestamp }
            .take(limit)
    }
} 