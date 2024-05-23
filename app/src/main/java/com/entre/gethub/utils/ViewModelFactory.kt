package com.entre.gethub.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.repositories.AuthRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.di.Injection
import com.entre.gethub.ui.auth.LoginViewModel
import com.entre.gethub.ui.auth.RegisterViewModel
import com.entre.gethub.ui.completeprofile.CompleteProfileViewModel
import com.entre.gethub.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SplashViewModel::class.java -> SplashViewModel(userPreferences) as T
            LoginViewModel::class.java -> LoginViewModel(authRepository, userPreferences) as T
            RegisterViewModel::class.java -> RegisterViewModel(authRepository) as T
            CompleteProfileViewModel::class.java -> CompleteProfileViewModel(profileRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory =
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideProfileRepository(context),
                Injection.provideUserPreferences(context),
            )
    }
}