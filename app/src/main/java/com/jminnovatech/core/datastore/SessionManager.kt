package com.jminnovatech.core.datastore

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    // ✅ ADD THIS
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}