package com.entre.gethub.ui.home.deteksiproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ml.ErrorResponse
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import com.entre.gethub.data.repositories.ProjectDetectorRepository
import com.entre.gethub.data.remote.retrofit.ApiMLService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class HomeProjectDetectorViewModel(
    private val projectDetectorRepository: ProjectDetectorRepository
) : ViewModel() {

    private val projectDetectorResult = MediatorLiveData<Result<ProjectDetectorResponse>>()

    // Tambahkan konstruktor nol argumen di sini
    constructor(apiMLService: ApiMLService) : this(ProjectDetectorRepository(apiMLService)) {}

    fun scanProject(imageFile: File): LiveData<Result<ProjectDetectorResponse>> {
        val requestImageFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image_file", imageFile.name, requestImageFile)

        val projectDetectorResult = MutableLiveData<Result<ProjectDetectorResponse>>()

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val result = projectDetectorRepository.scanFraudProject(imagePart)
                projectDetectorResult.value = Result.Success(result)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java).message
                projectDetectorResult.value = Result.Error(errorMessage ?: "An error occurred")
            } catch (e: Exception) {
                projectDetectorResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }

        return projectDetectorResult
    }
}
