package com.r9software.wall.app.util

import android.content.Context
import android.text.TextUtils

class SharedPreferencesUtil private constructor() {

    fun isLoggedIn(context: Context): Boolean {
        return !TextUtils.isEmpty(context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString("wall-app-token", ""))
    }

    fun setToken(context: Context, value: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString("wall-app-token", value).apply()
    }

    fun deleteToken(context:Context) {
        context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit().remove("wall-app-token").apply()
    }

    fun getToken(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString("wall-app-token", "").toString()
    }

    companion object {
        private var instance: SharedPreferencesUtil? = null
        private val PREFS_NAME = "default_preferences_wall_app"

        @Synchronized
        fun getInstance(): SharedPreferencesUtil {
            if (instance == null) {
                instance = SharedPreferencesUtil()
            }
            return instance as SharedPreferencesUtil
        }
    }
}