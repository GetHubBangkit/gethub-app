package com.entre.gethub.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.data.repositories.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val result = MediatorLiveData<Result<LoginResponse>>()
    val canNavigate = MediatorLiveData(false)

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        viewModelScope.launch {
            result.value = Result.Loading
            try {
                val response = authRepository.login(email, password)
                if (response.success == true) {
                    val user = response.user
                    result.value = Result.Success(response)
                    Log.d(TAG, "User data: $response")
                    Log.d(TAG, "Token: ${user?.token}")
                    userPreferences.apply {
                        saveUserLoginStatus(true)
                        saveToken(user?.token!!)
                        saveUserQRCode(user.qrCode.toString())
                        saveUserEmail(user?.email.toString())
                    }
                    canNavigate.value = true
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                result.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                result.value = Result.Error(e.message.toString())
            }
        }
        return result
    }

    fun getUserEmail(): LiveData<String> {
        return userPreferences.getUserEmail().asLiveData()
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}