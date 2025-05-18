package com.example.namhockey.data.models

data class News(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: Long,
    val author: String,
    val imageUrl: String?,
    val tags: List<String>
) 