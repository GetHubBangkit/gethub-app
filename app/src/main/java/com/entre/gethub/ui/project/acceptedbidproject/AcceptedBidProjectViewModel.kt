package com.entre.gethub.ui.project.acceptedbidproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AcceptedBidProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getAcceptedBidResult = MediatorLiveData<Result<AcceptedProjectBidResponse>>()

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