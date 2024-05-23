package com.entre.gethub.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val QR_CODE_KEY = stringPreferencesKey("qr_code")
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")

    fun getToken(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { pref ->
            pref[TOKEN_KEY] = token
        }
    }

    fun getUserLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[IS_LOGGED_IN_KEY] ?: false
        }
    }

    suspend fun saveUserLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { pref ->
            pref[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    fun getUserQRCode(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[QR_CODE_KEY] ?: ""
        }
    }

    suspend fun saveUserQRCode(qrContent: String) {
        dataStore.edit { pref ->
            pref[QR_CODE_KEY] = qrContent
        }
    }

    fun getUserEmail(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USER_EMAIL_KEY] ?: ""
        }
    }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit { pref ->
            pref[USER_EMAIL_KEY] = email
        }
    }

    companion object {
        fun getInstance(context: Context) = UserPreferences(context.dataStore)
    }

}