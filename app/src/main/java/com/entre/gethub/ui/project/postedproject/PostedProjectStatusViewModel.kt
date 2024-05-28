package com.entre.gethub.ui.project.postedproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostedProjectStatusViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getPostedProjectsResult = MediatorLiveData<Result<PostedProjectResponse>>()

    fun getPostedProjects(): LiveData<Result<PostedProjectResponse>> {
        viewModelScope.launch {
            getPostedProjectsResult.value = Result.Loading
            try {
                val response = projectRepository.getPostedProjects()

                if (response.success == true) {
                    getPostedProjectsResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getPostedProjectsResult.value = Result.Empty(errorMessage!!)
                }
                getPostedProjectsResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getPostedProjectsResult.value = Result.Error(e.toString())
            }
        }
        return getPostedProjectsResult
    }
}