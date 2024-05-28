package com.entre.gethub.ui.home.projectbids

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.ProjectsResponse
import com.entre.gethub.data.remote.response.projects.SearchProjectResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeCariProjectBidsViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val getAllProjectsResult = MediatorLiveData<Result<ProjectsResponse>>()
    private val searchProjectsResult = MediatorLiveData<Result<SearchProjectResponse>>()

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
                Log.e(TAG, "getProjects: $e")
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getAllProjectsResult.value = Result.Error(errorMessage!!)
                Log.e(TAG, "getProjects error msg: $errorMessage")
            } catch (e: Exception) {
                getAllProjectsResult.value = Result.Error(e.toString())
                Log.e(TAG, "getProjects: $e")
            }
        }
        return getAllProjectsResult
    }

    fun searchProjects(title: String): LiveData<Result<SearchProjectResponse>> {
        viewModelScope.launch {
            searchProjectsResult.value = Result.Loading
            try {
                val response = projectRepository.searchProjects(title)
                if (response.success == true) {
                    searchProjectsResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    searchProjectsResult.value = Result.Empty(errorMessage!!)
                }
                searchProjectsResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                searchProjectsResult.value = Result.Error(e.toString())
            }
        }
        return searchProjectsResult
    }

    companion object {
        private const val TAG = "HomeCariProjectBidsViewModel"
    }
}