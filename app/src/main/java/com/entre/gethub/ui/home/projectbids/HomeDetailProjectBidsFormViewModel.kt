package com.entre.gethub.ui.home.projectbids

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeDetailProjectBidsFormViewModel(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val bidProjectResult = MediatorLiveData<Result<ApiResponse>>()

    fun bidProject(
        projectId: String,
        budgetBid: Int,
        message: String
    ): LiveData<Result<ApiResponse>> {
        viewModelScope.launch {
            bidProjectResult.value = Result.Loading
            try {
                val response = projectRepository.bidProject(projectId, budgetBid, message)
                if (response.success == true) {
                    bidProjectResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(403)) {
                    bidProjectResult.value = Result.Empty(errorMessage!!)
                }
                bidProjectResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                bidProjectResult.value = Result.Error(e.toString())
            }
        }
        return bidProjectResult
    }
}