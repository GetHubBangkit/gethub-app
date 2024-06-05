package com.entre.gethub.ui.project.ownerpostedproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectDetailResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostedProjectStatusDetailViewModel(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val getPostedProjectDetailResult =
        MediatorLiveData<Result<PostedProjectDetailResponse>>()
    private val chooseBidderResult = MediatorLiveData<Result<ApiResponse>>()

    fun getPostedProjectDetail(id: String): LiveData<Result<PostedProjectDetailResponse>> {
        viewModelScope.launch {
            getPostedProjectDetailResult.value = Result.Loading
            try {
                val response = projectRepository.getPostedProjectDetail(id)
                if (response.success == true) {
                    getPostedProjectDetailResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getPostedProjectDetailResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getPostedProjectDetailResult.value = Result.Error(e.toString())
            }
        }
        return getPostedProjectDetailResult
    }

    fun chooseBidder(projectId: String, freelancerId: String): LiveData<Result<ApiResponse>> {
        viewModelScope.launch {
            chooseBidderResult.value = Result.Loading
            try {
                val response = projectRepository.chooseBidder(projectId, freelancerId)
                if (response.success == true) {
                    chooseBidderResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                chooseBidderResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                chooseBidderResult.value = Result.Error(e.toString())
            }
        }
        return chooseBidderResult
    }
}