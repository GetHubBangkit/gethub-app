package com.entre.gethub.ui.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.ProjectsResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.entre.gethub.ui.home.projectbids.HomeCariProjectBidsViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getAllProjectsResult = MediatorLiveData<Result<ProjectsResponse>>()

    fun getProjects(): LiveData<Result<ProjectsResponse>> {
        viewModelScope.launch {
            getAllProjectsResult.value = Result.Loading
            try {
                val response = projectRepository.getProjects()

                if (response.success == true) {
                    if (response.data?.totalData == 0) {
                        getAllProjectsResult.value = Result.Empty(response.message.toString())
                        return@launch
                    }
                    getAllProjectsResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getAllProjectsResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getAllProjectsResult.value = Result.Error(e.toString())
            }
        }
        return getAllProjectsResult
    }
}