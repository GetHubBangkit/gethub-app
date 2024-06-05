package com.entre.gethub.ui.userpublicprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.PostCardViewersResponse
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.data.repositories.UserPublicProfileRepository
import com.entre.gethub.data.repositories.PostCardViewersRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserPublicProfileViewModel(
    private val userPublicProfileRepository: UserPublicProfileRepository,
    private val postCardViewersRepository: PostCardViewersRepository
) : ViewModel() {

    private val _getLinkResult = MutableLiveData<Result<List<UserPublicProfileResponse.Data.Link>>>()
    val getLinkResult: LiveData<Result<List<UserPublicProfileResponse.Data.Link>>> get() = _getLinkResult

    private val _getProductListResult = MutableLiveData<Result<List<UserPublicProfileResponse.Data.Product>>>()
    val getProductListResult: LiveData<Result<List<UserPublicProfileResponse.Data.Product>>> get() = _getProductListResult

    private val _getCertificationsListResult = MutableLiveData<Result<List<UserPublicProfileResponse.Data.Certifications>>>()
    val getCertificationsListResult: LiveData<Result<List<UserPublicProfileResponse.Data.Certifications>>> get() = _getCertificationsListResult

    private val _getProjectsListResult = MutableLiveData<Result<List<UserPublicProfileResponse.Data.Projects>>>()
    val getProjectsListResult: LiveData<Result<List<UserPublicProfileResponse.Data.Projects>>> get() = _getProjectsListResult

    private val _userProfileResult = MutableLiveData<Result<UserPublicProfileResponse>>()
    val userProfileResult: LiveData<Result<UserPublicProfileResponse>> get() = _userProfileResult

    private val _postCardViewersResult = MutableLiveData<Result<PostCardViewersResponse>>()
    val postCardViewersResult: LiveData<Result<PostCardViewersResponse>> get() = _postCardViewersResult


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

    fun getCertifications(username: String): LiveData<Result<List<UserPublicProfileResponse.Data.Certifications>>> {
        viewModelScope.launch {
            try {
                val certification = userPublicProfileRepository.getCertifications(username)
                _getCertificationsListResult.value = Result.Success(certification)
            } catch (e: Exception) {
                _getCertificationsListResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return getCertificationsListResult
    }

    fun getProjects(username: String): LiveData<Result<List<UserPublicProfileResponse.Data.Projects>>> {
        viewModelScope.launch {
            try {
                val projects = userPublicProfileRepository.getProjects(username)
                _getProjectsListResult.value = Result.Success(projects)
            } catch (e: Exception) {
                _getProjectsListResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return getProjectsListResult
    }

    fun postCardViewers(username: String): LiveData<Result<PostCardViewersResponse>> {
        viewModelScope.launch {
            _postCardViewersResult.value = Result.Loading
            try {
                val response = postCardViewersRepository.postCardViewers(username)
                _postCardViewersResult.value = Result.Success(response)
            } catch (e: HttpException) {
                val errorMessage = e.message()
                _postCardViewersResult.value = Result.Error(errorMessage ?: "Unknown Error")
            } catch (e: Exception) {
                _postCardViewersResult.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
        return postCardViewersResult
    }

    companion object {
        private const val TAG = "UserPublicProfileViewModel"
    }
}
