package com.entre.gethub.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.repositories.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel( private val authRepository: AuthRepository) : ViewModel() {
    val result = MediatorLiveData<Result<ApiResponse>>()

    fun register(
        fullname: String,
        email: String,
        password: String
    ): LiveData<Result<ApiResponse>> {
        viewModelScope.launch {
            result.value = Result.Loading
            try {
                val response = authRepository.register(fullname, email, password)
                if (response.success == true) {
                    result.value = Result.Success(response)
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
}