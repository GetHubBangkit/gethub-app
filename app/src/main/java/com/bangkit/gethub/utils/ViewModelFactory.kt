package com.bangkit.gethub.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.gethub.data.repositories.AuthRepository
import com.bangkit.gethub.data.repositories.ProfileRepository
import com.bangkit.gethub.di.Injection
import com.bangkit.gethub.ui.auth.RegisterViewModel
import com.bangkit.gethub.ui.completeprofile.CompleteProfileViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
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
            )
    }
}