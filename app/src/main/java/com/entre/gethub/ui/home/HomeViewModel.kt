package com.entre.gethub.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.InformationHubResponse
import com.entre.gethub.data.remote.response.NewPartnerResponse
import com.entre.gethub.data.remote.response.TopTalentResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.data.repositories.NewPartnerRepository
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val informationHubRepository: InformationHubRepository,
    private val newPartnerRepository: NewPartnerRepository,
    private val projectRepository: ProjectRepository,
) : ViewModel() {
    private val getNewPartnerResult = MediatorLiveData<Result<NewPartnerResponse>>()
    private val _informationHubs =
        MutableLiveData<Result<List<InformationHubResponse.Data>>>(Result.Loading)
    val informationHubs: LiveData<Result<List<InformationHubResponse.Data>>> get() = _informationHubs
    private val getAcceptedBidResult = MediatorLiveData<Result<AcceptedProjectBidResponse>>()


    init {
        getInformationHubs()
    }

    fun getInformationHubs() {
        viewModelScope.launch {
            _informationHubs.value = Result.Loading
            try {
                val response = informationHubRepository.getInformationHub()
                if (response.success == true) {
                    _informationHubs.value = Result.Success(response.data ?: emptyList())
                } else {
                    _informationHubs.value = Result.Error(response.message ?: "Unknown Error")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    _informationHubs.value = Result.Empty("Belum Ada Informasi")
                }
                _informationHubs.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                _informationHubs.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
    }

    fun getNewPartner(): LiveData<Result<NewPartnerResponse>> {
        viewModelScope.launch {
            getNewPartnerResult.value = Result.Loading
            try {
                val response = newPartnerRepository.getNewPartner()
                if (response.success == true) {
                    getNewPartnerResult.value = Result.Success(response)
                }

            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code() == 404) {
                    getNewPartnerResult.value = Result.Empty(errorMessage!!)
                }
                getNewPartnerResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getNewPartnerResult.value = Result.Error(e.toString())
            }
        }
        return  getNewPartnerResult
    }

    fun getAcceptedBid(): LiveData<Result<AcceptedProjectBidResponse>> {
        viewModelScope.launch {
            getAcceptedBidResult.value = Result.Loading
            try {
                val response = projectRepository.getAcceptedBids()
                if (response.success == true) {
                    getAcceptedBidResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getAcceptedBidResult.value = Result.Empty(errorMessage.toString())
                }
                getAcceptedBidResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getAcceptedBidResult.value = Result.Error(e.toString())
            }
        }
        return getAcceptedBidResult
    }
}
