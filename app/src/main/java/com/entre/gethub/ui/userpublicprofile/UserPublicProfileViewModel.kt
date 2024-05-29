package com.entre.gethub.ui.userpublicprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.data.repositories.UserPublicProfileRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserPublicProfileViewModel(private val userPublicProfileRepository: UserPublicProfileRepository) : ViewModel() {

    private val _getLinkResult = MutableLiveData<Result<List<UserPublicProfileResponse.Data.Link>>>()
    val getLinkResult: LiveData<Result<List<UserPublicProfileResponse.Data.Link>>> get() = _getLinkResult

    private val _getProductListResult = MutableLiveData<Result<List<UserPublicProfileResponse.Data.Product>>>()
    val getProductListResult: LiveData<Result<List<UserPublicProfileResponse.Data.Product>>> get() = _getProductListResult

    private val _userProfileResult = MutableLiveData<Result<UserPublicProfileResponse>>()
    val userProfileResult: LiveData<Result<UserPublicProfileResponse>> get() = _userProfileResult

    fun getPublicProfile(username: String): LiveData<Result<UserPublicProfileResponse>> {
        viewModelScope.launch {
            _userProfileResult.value = Result.Loading
            try {
                val response = userPublicProfileRepository.getPublicProfile(username)
                _userProfileResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val errorMessage = e.message()
                _userProfileResult.value = Result.Error(errorMessage ?: "Unknown Error")
            } catch (e: Exception) {
                _userProfileResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return userProfileResult
    }

    fun getLinks(username: String): LiveData<Result<List<UserPublicProfileResponse.Data.Link>>> {
        viewModelScope.launch {
            try {
                val links = userPublicProfileRepository.getLinks(username)
                _getLinkResult.value = Result.Success(links)
            } catch (e: Exception) {
                _getLinkResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return getLinkResult
    }

    fun getProducts(username: String): LiveData<Result<List<UserPublicProfileResponse.Data.Product>>> {
        viewModelScope.launch {
            try {
                val products = userPublicProfileRepository.getProducts(username)
                _getProductListResult.value = Result.Success(products)
            } catch (e: Exception) {
                _getProductListResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return getProductListResult
    }

    companion object {
        private const val TAG = "UserPublicProfileViewModel"
    }
}

