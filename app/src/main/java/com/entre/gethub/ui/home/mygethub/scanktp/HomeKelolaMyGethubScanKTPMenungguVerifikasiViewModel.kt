package com.entre.gethub.ui.home.mygethub.scanktp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.ml.ErrorResponse
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.data.remote.response.ml.ScanKTPResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.ProfileRepository

import com.entre.gethub.data.repositories.ScanKTPRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class HomeKelolaMyGethubScanKTPMenungguVerifikasiViewModel
    (
    private val scanKTPRepository: ScanKTPRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val scanKTPResult = MediatorLiveData<Result<ScanKTPResponse>>()
    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()

    fun uploadScanKTP(imageFile: File): LiveData<Result<ScanKTPResponse>> {
        val requestImageFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image_file", imageFile.name, requestImageFile)

        val scanKTPResult = MutableLiveData<Result<ScanKTPResponse>>()

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val result = scanKTPRepository.scanKTP(imagePart)
                scanKTPResult.value = Result.Success(result)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java).message
                scanKTPResult.value = Result.Error(errorMessage ?: "An error occurred")
            } catch (e: Exception) {
                scanKTPResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }

        return scanKTPResult
    }
    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            getUserProfileResult.value = Result.Loading
            try {
                val response = profileRepository.getUserProfile()
                if (response.success == true) {
                    getUserProfileResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getUserProfileResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getUserProfileResult.value = Result.Error(e.message.toString())
            }
        }
        return getUserProfileResult
    }
}