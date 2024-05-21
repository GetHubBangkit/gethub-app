package com.bangkit.gethub.di

import android.content.Context
import com.bangkit.gethub.data.preferences.UserPreferences
import com.bangkit.gethub.data.remote.retrofit.ApiConfig
import com.bangkit.gethub.data.repositories.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return AuthRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences.getInstance(context)
    }
}