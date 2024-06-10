package com.entre.gethub.ui.home.caritalent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.CariTalentRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeCariTalentViewModel(
    private val cariTalentRepository: CariTalentRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _searchCariTalentResult = MediatorLiveData<Result<CariTalentResponse>>()
    val searchCariTalentResult: LiveData<Result<CariTalentResponse>> get() = _searchCariTalentResult

    private val _allTalentsResult = MediatorLiveData<Result<CariTalentResponse>>()
    val allTalentsResult: LiveData<Result<CariTalentResponse>> get() = _allTalentsResult

    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()

    fun searchCariTalent(profession: String): LiveData<Result<CariTalentResponse>> {
        _searchCariTalentResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HomeCariTalentViewModel", "Searching for profession: $profession")
                val response = cariTalentRepository.getCariTalent(profession)
                if (response.data.isNullOrEmpty()) {
                    _searchCariTalentResult.postValue(Result.Empty("No talents found"))
                } else {
                    _searchCariTalentResult.postValue(Result.Success(response))
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _searchCariTalentResult.postValue(Result.Error(errorMessage))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _searchCariTalentResult.postValue(Result.Error(errorMessage))
            }
        }
        return searchCariTalentResult
    }

    fun getAllTalents(): LiveData<Result<CariTalentResponse>> {
        _allTalentsResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HomeCariTalentViewModel", "Fetching all talents")
                val response = cariTalentRepository.getCariTalent("")
                if (response.data.isNullOrEmpty()) {
                    _allTalentsResult.postValue(Result.Empty("No talents found"))
                } else {
                    _allTalentsResult.postValue(Result.Success(response))
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _allTalentsResult.postValue(Result.Error(errorMessage))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _allTalentsResult.postValue(Result.Error(errorMessage))
            }
        }
        return allTalentsResult
    }
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
                getUserProfileResult.value = Result.Error(errorMessage ?: "An error occurred")
            } catch (e: Exception) {
                getUserProfileResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }
        return getUserProfileResult
    }
}
