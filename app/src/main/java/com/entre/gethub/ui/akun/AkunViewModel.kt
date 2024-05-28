package com.entre.gethub.ui.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.ProfileRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AkunViewModel(
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            getUserProfileResult.value = Result.Loading
            try {
                val response = profileRepository.getUserProfile()
                if (response.success == true) {
                    getUserProfileResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getUserProfileResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getUserProfileResult.value = Result.Error(e.message.toString())
            }
        }
        return getUserProfileResult
    }

    fun getToken(): LiveData<String> {
        return userPreferences.getToken().asLiveData()
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