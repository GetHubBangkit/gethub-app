package com.entre.gethub.ui.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.VisibilityResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.VisibilityRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AkunViewModel(
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences,
    private val visibilityRepository: VisibilityRepository
) : ViewModel() {
    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()
    private val visibilityLiveData = MediatorLiveData<Result<Boolean>>()
    private val setVisibilityResult = MutableLiveData<Result<VisibilityResponse>>()

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            getUserProfileResult.value = Result.Loading
            try {
                val response = profileRepository.getUserProfile()
                if (response.success == true) {
                    getUserProfileResult.value = Result.Success(response)
                } else {
                    getUserProfileResult.value = Result.Error("Failed to load user profile")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getUserProfileResult.value = Result.Error(errorMessage ?: "Unknown error")
            } catch (e: Exception) {
                e.printStackTrace()
                getUserProfileResult.value = Result.Error(e.message ?: "Unknown error")
            }
        }
        return getUserProfileResult
    }

    fun getToken(): LiveData<String> {
        return userPreferences.getToken().asLiveData()
    }

    fun getVisibility(): LiveData<Result<Boolean>> {
        viewModelScope.launch {
            visibilityLiveData.value = Result.Loading
            try {
                val isVisible = profileRepository.getVisibility()
                visibilityLiveData.value = Result.Success(isVisible)
            } catch (e: Exception) {
                visibilityLiveData.value = Result.Error(e.message ?: "An error occurred")
            }
        }
        return visibilityLiveData
    }

    fun setVisibility(isVisible: Boolean): LiveData<Result<VisibilityResponse>> {
        viewModelScope.launch {
            setVisibilityResult.value = Result.Loading
            try {
                val response = visibilityRepository.setVisibility(isVisible)
                if (response.success == true) {
                    setVisibilityResult.value = Result.Success(response)
                } else {
                    setVisibilityResult.value = Result.Error(response.message ?: "Failed to set visibility")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, VisibilityResponse::class.java)
                val errorMessage = errorBody.message
                setVisibilityResult.value = Result.Error(errorMessage ?: "Unknown error")
            } catch (e: Exception) {
                setVisibilityResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }
        return setVisibilityResult
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.apply {
                saveToken("")
                saveUserLoginStatus(false)
            }
        }
    }
}
