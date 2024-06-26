package com.entre.gethub.di

import android.content.Context
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.retrofit.ApiConfig
import com.entre.gethub.data.repositories.AnaliticTotalRepository
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.CardViewersRepository
import com.entre.gethub.data.repositories.CariTalentRepository
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.CertificationRepository
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.repositories.GraphDataRepository
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.LinkRepository
import com.entre.gethub.data.repositories.NewPartnerRepository
import com.entre.gethub.data.repositories.PaymentHistoryRepository
import com.entre.gethub.data.repositories.PostCardViewersRepository
import com.entre.gethub.data.repositories.PremiumRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.ProjectDetectorRepository
import com.entre.gethub.data.repositories.ProjectRepository
import com.entre.gethub.data.repositories.ReysEventRepository
import com.entre.gethub.data.repositories.ScanCardRepository
import com.entre.gethub.data.repositories.ScanKTPRepository
import com.entre.gethub.data.repositories.SponsorRepository
import com.entre.gethub.data.repositories.ThemeHubRepository
import com.entre.gethub.data.repositories.TopTalentRepository
import com.entre.gethub.data.repositories.UserPublicProfileRepository
import com.entre.gethub.data.repositories.VisibilityRepository
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

    fun provideCertificationRepository(context: Context): CertificationRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return CertificationRepository.getInstance(ApiConfig.getApiService(context, token))
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

    fun provideProjectDetectorRepository(context: Context): ProjectDetectorRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ProjectDetectorRepository.getInstance(ApiConfig.getApiMLService(context, token))
    }

    fun provideCariTalentRepository(context: Context): CariTalentRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return CariTalentRepository.getInstance(ApiConfig.getApiMLService(context, token))
    }

    fun provideUserPublicProfileRepository(context: Context): UserPublicProfileRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return UserPublicProfileRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideVisibilityRepository(context: Context): VisibilityRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return VisibilityRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideThemeHubRepository(context: Context): ThemeHubRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ThemeHubRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideTopTalentRepository(context: Context): TopTalentRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return TopTalentRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideAnaliticTotalRepository(context: Context): AnaliticTotalRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return AnaliticTotalRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideNewPartnerRepository(context: Context): NewPartnerRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return NewPartnerRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideCardViewersRepository(context: Context): CardViewersRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return CardViewersRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun providePostCardViewersRepository(context: Context): PostCardViewersRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return PostCardViewersRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun providePremiumRepository(context: Context): PremiumRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return PremiumRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideGraphDataRepository(context: Context): GraphDataRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return GraphDataRepository.getInstance(ApiConfig.getApiService(context, token))
    }

    fun provideReysEventRepository(context: Context): ReysEventRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ReysEventRepository.getInstance(ApiConfig.getApiMLService(context, token))
    }
        fun providePaymentHistoryRepository(context: Context): PaymentHistoryRepository {
            val pref = provideUserPreferences(context)
            val token = runBlocking { pref.getToken().first() }
            return PaymentHistoryRepository.getInstance(ApiConfig.getApiService(context, token))
        }

    fun provideScanKTPRepository(context: Context): ScanKTPRepository {
        val pref = provideUserPreferences(context)
        val token = runBlocking { pref.getToken().first() }
        return ScanKTPRepository.getInstance(ApiConfig.getApiService(context, token))
    }
    }
