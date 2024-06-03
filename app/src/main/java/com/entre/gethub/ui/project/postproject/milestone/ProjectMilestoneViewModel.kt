package com.entre.gethub.ui.project.postproject.milestone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.AllProjectMilestoneResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProjectMilestoneViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getProjectMilestoneResult = MediatorLiveData<Result<AllProjectMilestoneResponse>>()

    fun getMilestone(projectId: String): LiveData<Result<AllProjectMilestoneResponse>> {
        viewModelScope.launch {
            getProjectMilestoneResult.value = Result.Loading
            try {
                val response = projectRepository.getMilestone(projectId)

                if (response.success == true) {
                    getProjectMilestoneResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getProjectMilestoneResult.value = Result.Empty(errorMessage!!)
                }
                getProjectMilestoneResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getProjectMilestoneResult.value = Result.Error(e.toString())
            }
        }
        return getProjectMilestoneResult
    }

}