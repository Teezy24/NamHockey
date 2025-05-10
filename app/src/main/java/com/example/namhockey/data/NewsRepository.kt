package com.example.namhockey.data

data class NewsItem(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: String
)

object NewsRepository {
    private val news = mutableListOf<NewsItem>()
    private var nextId = 1

    fun addNews(item: NewsItem) {
        news.add(item.copy(id = nextId++))
    }

    fun removeNews(newsId: Int) {
        news.removeAll { it.id == newsId }
    }

    fun getNews(): List<NewsItem> = news
} 