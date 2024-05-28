package com.entre.gethub.ui.home.projectbids

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.ProjectDetailResponse
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeDetailProjectBidsViewModel(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val getDetailProjectResult = MediatorLiveData<Result<ProjectDetailResponse>>()

    fun getProjectDetail(id: String): LiveData<Result<ProjectDetailResponse>> {
        viewModelScope.launch {
            getDetailProjectResult.value = Result.Loading
            try {
                val response = projectRepository.getProjectDetail(id)
                if (response?.success == true) {
                    getDetailProjectResult.value = Result.Success(response)
                } else {
                    getDetailProjectResult.value = Result.Error("Failed to get project details")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                if (jsonString != null) {
                    val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                    val errorMessage = errorBody?.message ?: "Unknown error"
                    getDetailProjectResult.value = Result.Error(errorMessage)
                } else {
                    getDetailProjectResult.value = Result.Error("Unknown error")
                }
                Log.e(TAG, "getProjectDetail: ${e.message}", e)
            } catch (e: Exception) {
                getDetailProjectResult.value = Result.Error(e.toString())
                Log.e(TAG, "getProjectDetail: ${e.message}", e)
            }
        }
        return getDetailProjectResult
    }


    companion object {
        private const val TAG = "HomeDetailProjectBidsViewModel"
    }
}