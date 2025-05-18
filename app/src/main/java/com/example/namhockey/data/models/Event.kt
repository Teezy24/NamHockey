package com.example.namhockey.data.models

data class Event(
    val id: String,
    val name: String,
    val date: String,
    val location: String,
    val description: String,
    val teamIds: List<String>,
    val type: String,
    val status: String
) 