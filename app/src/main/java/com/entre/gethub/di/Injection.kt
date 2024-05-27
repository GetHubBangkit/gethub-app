package com.entre.gethub.di

import android.content.Context
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.retrofit.ApiConfig
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.LinkRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.ProjectRepository
import com.entre.gethub.data.repositories.ScanCardRepository
import com.entre.gethub.data.repositories.SponsorRepository
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

    fun provideGethubRepository(context: Context): GethubRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return GethubRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideSponsorRepository(context: Context): SponsorRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return SponsorRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideProductRepository(context: Context): ProductRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ProductRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideLinkRepository(context: Context): LinkRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return LinkRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideCategoryRepository(context: Context): CategoryRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return CategoryRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideProjectRepository(context: Context): ProjectRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ProjectRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    // ML API Service
    fun provideScanCardRepository(context: Context): ScanCardRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ScanCardRepository.getInstance(ApiConfig.getApiMLService(context, token))
    }
}