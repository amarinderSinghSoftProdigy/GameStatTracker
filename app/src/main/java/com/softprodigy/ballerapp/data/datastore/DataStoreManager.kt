package com.softprodigy.ballerapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val settingsDataStore = appContext.dataStore

    suspend fun saveToken(token: String) {
        settingsDataStore.edit { settings ->
            settings[USER_TOKEN] = token
        }
    }

    val userToken: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[USER_TOKEN] ?: ""
    }

    suspend fun setRole(role: String) {
        settingsDataStore.edit { settings ->
            settings[ROLE] = role
        }
    }

    val getRole: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[ROLE] ?: ""
    }

    suspend fun setTap(tap: String) {
        settingsDataStore.edit { settings ->
            settings[USER_TAP] = tap
        }
    }

    val getTap: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[USER_TAP] ?: ""
    }

    suspend fun setUserData(userData: String) {
        settingsDataStore.edit { settings ->
            settings[USER_DATA] = userData
        }
    }

    val getUserName: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[USER_DATA] ?: ""
    }

    val getColor: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[COLOR] ?: ""
    }

    val getWalkThrough: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[SKIP_WALKTHROUGH] ?: ""
    }
    suspend fun skipWalkthrough(color: String) {
        settingsDataStore.edit { settings ->
            settings[SKIP_WALKTHROUGH] = color
        }
    }
    suspend fun setColor(color: String) {
        settingsDataStore.edit { settings ->
            settings[COLOR] = color
        }
    }

    companion object {
        val USER_TOKEN = stringPreferencesKey("USER_TOKEN")
        val ROLE = stringPreferencesKey("ROLE")
        val LOGOUT = stringPreferencesKey("LOGOUT")
        val USER_TAP = stringPreferencesKey("USER_TAP")
        val USER_DATA = stringPreferencesKey("USER_DATA")
        val COLOR = stringPreferencesKey("COLOR")
        val SKIP_WALKTHROUGH = stringPreferencesKey("SKIP_WALKTHROUGH")
    }
}
