package com.entre.gethub.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.ProjectStatsResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getUserProjectStats = MediatorLiveData<Result<ProjectStatsResponse>>()

    fun getUserProjectStats(): LiveData<Result<ProjectStatsResponse>> {
        viewModelScope.launch {
            getUserProjectStats.value = Result.Loading
            try {
                val response = projectRepository.getUserProjectStats()
                if (response.success == true) {
                    getUserProjectStats.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getUserProjectStats.value = Result.Empty(errorMessage!!)
                }
                getUserProjectStats.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getUserProjectStats.value = Result.Error(e.toString())
            }
        }
        return getUserProjectStats
    }
}