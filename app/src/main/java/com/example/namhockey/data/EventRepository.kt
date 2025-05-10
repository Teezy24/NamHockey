package com.example.namhockey.data

data class Event(
    val id: Int,
    val name: String,
    val date: String,
    val location: String,
    val description: String,
    val teamIds: MutableList<Int> = mutableListOf()
)

object EventRepository {
    private val events = mutableListOf<Event>()
    private var nextId = 1

    fun addEvent(event: Event) {
        events.add(event.copy(id = nextId++))
    }

    fun removeEvent(eventId: Int) {
        events.removeAll { it.id == eventId }
    }

    fun getEvents(): List<Event> = events
    fun enterTeam(eventId: Int, teamId: Int) {
        events.find { it.id == eventId }?.teamIds?.add(teamId)
    }
} 