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

    suspend fun setUserType(userInfo: String) {
        settingsDataStore.edit { settings ->
            settings[USER_KEY] = userInfo
        }
    }

    val userInfo: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[USER_KEY] ?: ""
    }

    suspend fun setOtp(otp: String) {
        settingsDataStore.edit { settings ->
            settings[OTP] = otp
        }
    }

    val getOtp: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[OTP] ?: ""
    }

    suspend fun setTap(tap: String) {
        settingsDataStore.edit { settings ->
            settings[USER_TAP] = tap
        }
    }

    val getTap: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[USER_TAP] ?: ""
    }

    suspend fun setUserData(username: String) {
        settingsDataStore.edit { settings ->
            settings[USER_NAME] = username
            //settings[PASSWORD] = password
        }
    }

    val getUserName: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    val getColor: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[COLOR] ?: ""
    }
    suspend fun setColor(color: String) {
        settingsDataStore.edit { settings ->
            settings[COLOR] = color
        }
    }

    companion object {
        val USER_KEY = stringPreferencesKey("USER_KEY")
        val OTP = stringPreferencesKey("OTP")
        val LOGOUT = stringPreferencesKey("LOGOUT")
        val USER_TAP = stringPreferencesKey("USER_TAP")
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val COLOR = stringPreferencesKey("COLOR")
    }
}
