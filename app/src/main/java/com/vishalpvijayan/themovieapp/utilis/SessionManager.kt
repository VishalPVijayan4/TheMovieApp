package com.vishalpvijayan.themovieapp.utilis

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
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

    fun isLoggedIn(): Boolean = !getSessionId().isNullOrBlank() && !isExpired()

    fun saveExpiry(expiresAt: String?) {
        prefs.edit().putString(KEY_EXPIRES_AT, expiresAt).apply()
    }

    fun getExpiry(): String? = prefs.getString(KEY_EXPIRES_AT, null)

    fun saveAccountId(accountId: Int) {
        prefs.edit().putInt(KEY_ACCOUNT_ID, accountId).apply()
    }

    fun getAccountId(): Int? {
        val value = prefs.getInt(KEY_ACCOUNT_ID, -1)
        return if (value <= 0) null else value
    }

    fun isExpired(): Boolean {
        val expiry = getExpiry() ?: return false
        return runCatching {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val time = format.parse(expiry.replace(" UTC", "")) ?: return@runCatching false
            time.before(Date())
        }.getOrDefault(false)
    }

    fun clearSession() {
        prefs.edit().remove(KEY_SESSION_ID).remove(KEY_EXPIRES_AT).remove(KEY_ACCOUNT_ID).apply()
    }

    private companion object {
        const val KEY_SESSION_ID = "session_id"
        const val KEY_EXPIRES_AT = "expires_at"
        const val KEY_ACCOUNT_ID = "account_id"
    }
}
