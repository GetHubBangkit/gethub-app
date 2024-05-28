package com.entre.gethub.ui.project.bidproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.ErrorResponse
import com.entre.gethub.data.remote.response.projects.MyProjectBidResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BidProjectStatusViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    val getMyProjectBidsResult = MediatorLiveData<Result<MyProjectBidResponse>>()

    fun getMyProjectBids(): LiveData<Result<MyProjectBidResponse>> {
        viewModelScope.launch {
            getMyProjectBidsResult.value = Result.Loading
            try {
                val response = projectRepository.getMyProjectBids()
                if (response.success == true) {
                    getMyProjectBidsResult.value = Result.Success(response)
                } else {
                    getMyProjectBidsResult.value =
                        Result.Empty("Anda belum melakukan Project Bidding")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                if (errorBody.data.isEmpty()) {
                    getMyProjectBidsResult.value = Result.Empty(errorMessage!!)
                    return@launch
                }
                getMyProjectBidsResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getMyProjectBidsResult.value = Result.Error(e.toString())
            }
        }
        return getMyProjectBidsResult
    }

}