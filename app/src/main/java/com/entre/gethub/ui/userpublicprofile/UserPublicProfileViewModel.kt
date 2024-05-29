package com.entre.gethub.ui.userpublicprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.data.repositories.UserPublicProfileRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class UserPublicProfileViewModel(private val userPublicProfileRepository: UserPublicProfileRepository) : ViewModel() {

    private val _userProfileResult = MutableLiveData<Result<UserPublicProfileResponse>>()

    fun getPublicProfile(username: String): LiveData<Result<UserPublicProfileResponse>> {
        viewModelScope.launch {
            _userProfileResult.value = Result.Loading
            try {
                val response = userPublicProfileRepository.getPublicProfile(username)
                _userProfileResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val errorMessage = e.message() // Menggunakan method message() untuk mendapatkan pesan kesalahan
                _userProfileResult.value = Result.Error(errorMessage ?: "Unknown Error")
            } catch (e: Exception) {
                _userProfileResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return _userProfileResult
    }
}
