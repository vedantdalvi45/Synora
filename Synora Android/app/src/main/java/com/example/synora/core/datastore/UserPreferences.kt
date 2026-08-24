package com.example.synora.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val KEY_IS_LOGGED_IN  = booleanPreferencesKey("is_logged_in")
        private val KEY_AUTH_TOKEN    = stringPreferencesKey("auth_token")
        private val KEY_THEME         = stringPreferencesKey("theme") // "light" | "dark" | "system"
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }
    val authToken: Flow<String?>  = dataStore.data.map { it[KEY_AUTH_TOKEN] }
    val theme: Flow<String>       = dataStore.data.map { it[KEY_THEME] ?: "system" }

    suspend fun setLoggedIn(value: Boolean) = dataStore.edit { it[KEY_IS_LOGGED_IN] = value }
    suspend fun setAuthToken(token: String?) = dataStore.edit {
        if (token != null) it[KEY_AUTH_TOKEN] = token else it.remove(KEY_AUTH_TOKEN)
    }
    suspend fun setTheme(theme: String) = dataStore.edit { it[KEY_THEME] = theme }

    suspend fun clear() = dataStore.edit { it.clear() }
}
