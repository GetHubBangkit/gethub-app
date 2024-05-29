package com.entre.gethub.ui.project.postproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.CategoriesResponse
import com.entre.gethub.data.remote.response.projects.PostProjectResponse
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.ProjectRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostProjectViewModel(
    private val categoryRepository: CategoryRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val getCategoriesResult = MediatorLiveData<Result<CategoriesResponse>>()
    private val postProjectResult = MediatorLiveData<Result<PostProjectResponse>>()

    fun getCategories(): LiveData<Result<CategoriesResponse>> {
        viewModelScope.launch {
            try {
                getCategoriesResult.value = Result.Loading
                val response = categoryRepository.getCategories()

                if (response.success == true) {
                    getCategoriesResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getCategoriesResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getCategoriesResult.value = Result.Error(e.toString())
            }
        }
        return getCategoriesResult
    }

    fun postProject(
        title: String,
        categoryId: String,
        description: String,
        minBudget: Int,
        maxBudget: Int,
        minDeadline: String,
        maxDeadline: String,
    ): LiveData<Result<PostProjectResponse>> {
        viewModelScope.launch {
            postProjectResult.value = Result.Loading
            try {
                val response = projectRepository.postProject(
                    title,
                    categoryId,
                    description,
                    minBudget,
                    maxBudget,
                    minDeadline,
                    maxDeadline,
                )

                if (response.success == true) {
                    postProjectResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(403)) {
                    postProjectResult.value =
                        Result.Empty("Anda belum melakukan verifikasi Email, Klik Kirim untuk mendapatkan Email verifikasi")
                    return@launch
                }
                postProjectResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                postProjectResult.value = Result.Error(e.toString())
            }
        }
        return postProjectResult
    }

    fun regenerateVerifyEmail() {
        viewModelScope.launch {
            try {
                projectRepository.regenerateVerifyEmail()
            } catch (e: HttpException) {
                Result.Error(e.message())
            }
        }
    }
}