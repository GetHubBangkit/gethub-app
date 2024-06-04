package com.entre.gethub.ui.project.postedproject.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.SettlementResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OwnerSettlementViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getSettlementResult = MediatorLiveData<Result<SettlementResponse>>()

    fun getSettlement(projectId: String): LiveData<Result<SettlementResponse>> {
        viewModelScope.launch {
            getSettlementResult.value = Result.Loading
            try {
                val response = projectRepository.getSettlement(projectId)

                if (response.success == true) {
                    getSettlementResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getSettlementResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getSettlementResult.value = Result.Error(e.toString())
            }
        }
        return getSettlementResult
    }
}