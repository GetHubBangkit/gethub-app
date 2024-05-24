package com.entre.gethub.ui.gethub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.preferences.UserPreferences
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.remote.response.SponsorResponse
import com.entre.gethub.data.remote.response.partners.GetHubPartnerListResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.SponsorRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GethubViewModel(
    private val gethubRepository: GethubRepository,
    private val sponsorRepository: SponsorRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {
    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            getUserProfileResult.value = Result.Loading
            try {
                val response = gethubRepository.getUserProfile()
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

    private val _sponsors = MediatorLiveData<Result<SponsorResponse>>()
    val sponsors: LiveData<Result<SponsorResponse>> get() = _sponsors

    fun getSponsors() {
        viewModelScope.launch {
            _sponsors.value = Result.Loading
            try {
                val response = sponsorRepository.getSponsors()
                if (response.success == true) {
                    _sponsors.value = Result.Success(response)
                } else {
                    _sponsors.value = Result.Error(response.message ?: "Unknown Error")
                }
            } catch (e: Exception) {
                _sponsors.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
    }

    private val getHubPartnerListResult = MediatorLiveData<Result<GetHubPartnerListResponse>>()

    fun getPartnerList(): LiveData<Result<GetHubPartnerListResponse>> {
        viewModelScope.launch {
            try {
                getHubPartnerListResult.value = Result.Loading
                val response = gethubRepository.getPartnerList()
                if (response.success == true) {
                    getHubPartnerListResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getHubPartnerListResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getHubPartnerListResult.value = Result.Error(e.toString())
            }
        }

        return getHubPartnerListResult
    }

    fun getUserQRCode(): LiveData<String> {
        return userPreferences.getUserQRCode().asLiveData()
    }

}

