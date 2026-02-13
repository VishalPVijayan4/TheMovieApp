package com.vishalpvijayan.themovieapp.utilis

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("tmdb_session", Context.MODE_PRIVATE)

    fun saveSessionId(sessionId: String) {
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply()
    }

    fun getSessionId(): String? = prefs.getString(KEY_SESSION_ID, null)

    fun isLoggedIn(): Boolean = !getSessionId().isNullOrBlank()

    fun clearSession() {
        prefs.edit().remove(KEY_SESSION_ID).apply()
    }

    private companion object {
        const val KEY_SESSION_ID = "session_id"
    }
}
