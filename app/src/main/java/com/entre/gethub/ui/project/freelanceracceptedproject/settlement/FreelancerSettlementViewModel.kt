package com.entre.gethub.ui.project.freelanceracceptedproject.settlement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.AllBanksResponse
import com.entre.gethub.data.remote.response.projects.FreelancerSettlementResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FreelancerSettlementViewModel(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val freelancerSettlementResult =
        MediatorLiveData<Result<FreelancerSettlementResponse>>()
    private val getBanksResult = MediatorLiveData<Result<AllBanksResponse>>()

    fun getFreelancerSettlement(projectId: String): LiveData<Result<FreelancerSettlementResponse>> {
        viewModelScope.launch {
            freelancerSettlementResult.value = Result.Loading
            try {
                val response = projectRepository.getSettlementFreelancer(projectId)
                if (response.success == true) {
                    freelancerSettlementResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                freelancerSettlementResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                freelancerSettlementResult.value = Result.Error(e.toString())
            }
        }
        return freelancerSettlementResult
    }

    fun getBanks(): LiveData<Result<AllBanksResponse>> {
        viewModelScope.launch {
            getBanksResult.value = Result.Loading
            try {
                val response = projectRepository.getBanks()
                if (response.success == true) {
                    getBanksResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getBanksResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getBanksResult.value = Result.Error(e.toString())
            }
        }
        return getBanksResult
    }
}