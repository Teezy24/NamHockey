package com.example.namhockey.data.models

data class Team(
    val id: String,
    val name: String,
    val city: String,
    val founded: Int,
    val homeVenue: String,
    val colors: List<String>,
    val logo: String,
    val description: String
) 