package com.entre.gethub.ui.project.ownerpostedproject.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.ReviewResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FreelancerReviewViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val reviewResult = MediatorLiveData<Result<ReviewResponse>>()

    fun reviewFreelancer(
        projectId: String,
        targetUserId: String,
        message: String,
        reviewType: String
    ): LiveData<Result<ReviewResponse>> {
        viewModelScope.launch {
            reviewResult.value = Result.Loading
            try {
                val response =
                    projectRepository.createReview(projectId, targetUserId, message, reviewType)
                if (response.success == true) {
                    reviewResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                reviewResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                reviewResult.value = Result.Error(e.toString())
            }
        }
        return reviewResult
    }
}