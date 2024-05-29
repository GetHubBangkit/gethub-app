package com.entre.gethub.ui.home.projectbids

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.SearchProjectResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    private val searchProjectsResult = MediatorLiveData<Result<SearchProjectResponse>>()

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
                    return@launch
                }
                searchProjectsResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                searchProjectsResult.value = Result.Error(e.toString())
            }
        }
        return searchProjectsResult
    }
}