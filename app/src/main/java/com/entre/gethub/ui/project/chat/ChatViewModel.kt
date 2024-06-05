package com.entre.gethub.ui.project.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.ProfileRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChatViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            try {
                getUserProfileResult.value = Result.Loading
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
                getUserProfileResult.value = Result.Error(e.toString())
            }
        }
        return getUserProfileResult
    }

}