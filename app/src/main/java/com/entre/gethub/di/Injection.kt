package com.entre.gethub.di

import android.content.Context
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.retrofit.ApiConfig
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return AuthRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ProfileRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences.getInstance(context)
    }

    fun provideInformationHubRepository(context: Context): InformationHubRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return InformationHubRepository.getInstance(ApiConfig.getApiService(context, token))
}