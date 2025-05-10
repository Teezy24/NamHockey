package com.example.namhockey.data

import android.content.Context
import android.content.SharedPreferences

object UserRepository {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_LOGGED_IN = "logged_in"
    private const val KEY_USERNAME = "username"
    private const val KEY_FAVORITE_TEAM = "favorite_team"

    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_LOGGED_IN, false)

    fun login(context: Context, username: String, password: String): Boolean {
        if (username.isNotBlank() && password.isNotBlank()) {
            getPrefs(context).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_USERNAME, username)
                .apply()
            return true
        }
        return false
    }

    fun logout(context: Context) {
        getPrefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, false)
            .remove(KEY_USERNAME)
            .apply()
    }

    fun getUsername(context: Context): String? =
        getPrefs(context).getString(KEY_USERNAME, null)

    fun setFavoriteTeam(context: Context, teamName: String) {
        getPrefs(context).edit().putString(KEY_FAVORITE_TEAM, teamName).apply()
    }

    fun getFavoriteTeam(context: Context): String? =
        getPrefs(context).getString(KEY_FAVORITE_TEAM, null)
} 