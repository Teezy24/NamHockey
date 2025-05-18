package com.example.namhockey.data

import android.content.Context
import com.example.namhockey.R
import com.example.namhockey.data.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedReader
import java.io.InputStreamReader

class ResourceRepository(private val context: Context) {
    private val gson = Gson()

    fun getTeams(): Flow<List<Team>> = flow {
        val json = readJsonFile(R.raw.teams)
        val type = object : TypeToken<Map<String, List<Team>>>() {}.type
        val data: Map<String, List<Team>> = gson.fromJson(json, type)
        emit(data["teams"] ?: emptyList())
    }.flowOn(Dispatchers.IO)

    fun getPlayers(): Flow<List<Player>> = flow {
        val json = readJsonFile(R.raw.players)
        val type = object : TypeToken<Map<String, List<Player>>>() {}.type
        val data: Map<String, List<Player>> = gson.fromJson(json, type)
        emit(data["players"] ?: emptyList())
    }.flowOn(Dispatchers.IO)

    fun getStandings(): Flow<StandingsData> = flow {
        val json = readJsonFile(R.raw.standings)
        val data: StandingsData = gson.fromJson(json, StandingsData::class.java)
        emit(data)
    }.flowOn(Dispatchers.IO)

    fun getEvents(): Flow<List<Event>> = flow {
        val json = readJsonFile(R.raw.events)
        val type = object : TypeToken<Map<String, List<Event>>>() {}.type
        val data: Map<String, List<Event>> = gson.fromJson(json, type)
        emit(data["events"] ?: emptyList())
    }.flowOn(Dispatchers.IO)

    fun getNews(): Flow<List<News>> = flow {
        val json = readJsonFile(R.raw.news)
        val type = object : TypeToken<Map<String, List<News>>>() {}.type
        val data: Map<String, List<News>> = gson.fromJson(json, type)
        emit(data["news"] ?: emptyList())
    }.flowOn(Dispatchers.IO)

    private fun readJsonFile(resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }

    companion object {
        @Volatile
        private var instance: ResourceRepository? = null

        fun getInstance(context: Context): ResourceRepository {
            return instance ?: synchronized(this) {
                instance ?: ResourceRepository(context.applicationContext).also { instance = it }
            }
        }
    }
} 