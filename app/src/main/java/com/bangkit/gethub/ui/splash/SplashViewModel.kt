package com.bangkit.gethub.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.gethub.data.preferences.UserPreferences

class SplashViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    fun getUserSignedInStatus(): LiveData<Boolean> {
        return userPreferences.getUserLoginStatus().asLiveData()
    }
}