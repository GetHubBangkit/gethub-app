package com.entre.gethub.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.LinkRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.SponsorRepository
import com.entre.gethub.di.Injection
import com.entre.gethub.ui.akun.AkunViewModel
import com.entre.gethub.ui.auth.LoginViewModel
import com.entre.gethub.ui.auth.RegisterViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileValidationViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileViewModel
import com.entre.gethub.ui.gethub.GethubAddPartnerFormViewModel
import com.entre.gethub.ui.gethub.GethubPartnerListViewModel
import com.entre.gethub.ui.gethub.GethubViewModel
import com.entre.gethub.ui.home.HomeViewModel
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubViewModel
import com.entre.gethub.ui.home.mygethub.link.HomeKelolaMyGethubTambahLinkViewModel
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubEditProdukViewModel
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubTambahProdukViewModel
import com.entre.gethub.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences,
    private val informationHubRepository: InformationHubRepository,
    private val gethubRepository: GethubRepository,
    private val sponsorRepository: SponsorRepository,
    private val productRepository: ProductRepository,
    private val linkRepository: LinkRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SplashViewModel::class.java -> SplashViewModel(userPreferences) as T
            LoginViewModel::class.java -> LoginViewModel(authRepository, userPreferences) as T
            RegisterViewModel::class.java -> RegisterViewModel(authRepository) as T
            HomeViewModel::class.java -> HomeViewModel(informationHubRepository) as T
            CompleteProfileViewModel::class.java -> CompleteProfileViewModel(profileRepository) as T
            CompleteProfileValidationViewModel::class.java -> CompleteProfileValidationViewModel(
                profileRepository
            ) as T

            AkunViewModel::class.java -> AkunViewModel(profileRepository, userPreferences) as T
            GethubViewModel::class.java -> GethubViewModel(
                gethubRepository,
                sponsorRepository,
                userPreferences
            ) as T

            HomeKelolaMyGethubViewModel::class.java -> HomeKelolaMyGethubViewModel(
                productRepository,
                linkRepository
            ) as T

            HomeKelolaMyGethubTambahProdukViewModel::class.java -> HomeKelolaMyGethubTambahProdukViewModel(
                productRepository, categoryRepository
            ) as T

            HomeKelolaMyGethubEditProdukViewModel::class.java -> HomeKelolaMyGethubEditProdukViewModel(
                productRepository
            ) as T

            HomeKelolaMyGethubTambahLinkViewModel::class.java -> HomeKelolaMyGethubTambahLinkViewModel(
                linkRepository
            ) as T

            GethubPartnerListViewModel::class.java -> GethubPartnerListViewModel(
                gethubRepository
            ) as T

            GethubAddPartnerFormViewModel::class.java -> GethubAddPartnerFormViewModel(
                gethubRepository
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory =
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideProfileRepository(context),
                Injection.provideUserPreferences(context),
                Injection.provideInformationHubRepository(context),
                Injection.provideGethubRepository(context),
                Injection.provideSponsorRepository(context),
                Injection.provideProductRepository(context),
                Injection.provideLinkRepository(context),
                Injection.provideCategoryRepository(context)
            )
    }
}
