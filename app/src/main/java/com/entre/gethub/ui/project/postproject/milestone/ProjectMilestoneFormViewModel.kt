package com.entre.gethub.ui.project.postproject.milestone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.AddProjectMilestoneResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProjectMilestoneFormViewModel(private val projectRepository: ProjectRepository) :
    ViewModel() {

    private val addProjectMilestoneResult = MediatorLiveData<Result<AddProjectMilestoneResponse>>()

    fun addMilestone(
        projectId: String,
        taskNumber: Int,
        taskDescription: String
    ): LiveData<Result<AddProjectMilestoneResponse>> {
        viewModelScope.launch {
            addProjectMilestoneResult.value = Result.Loading
            try {
                val response =
                    projectRepository.addMilestone(projectId, taskNumber, taskDescription)
                if (response.success == true) {
                    addProjectMilestoneResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                addProjectMilestoneResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                addProjectMilestoneResult.value = Result.Error(e.toString())
            }
        }
        return addProjectMilestoneResult
    }

}