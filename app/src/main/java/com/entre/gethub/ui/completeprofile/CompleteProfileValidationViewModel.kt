package com.entre.gethub.ui.completeprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.ml.ErrorResponse
import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.ScanCardRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class CompleteProfileValidationViewModel(
    private val profileRepository: ProfileRepository,
    private val scanCardRepository: ScanCardRepository
) :
    ViewModel() {

    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()
    private val scanCardResult = MediatorLiveData<Result<ScanCardResponse>>()

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            try {
                getUserProfileResult.value = Result.Loading
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
                getUserProfileResult.value = Result.Error(e.toString())
            }
        }
        return getUserProfileResult
    }

    fun scanCard(imageFile: File): LiveData<Result<ScanCardResponse>> {
        viewModelScope.launch {
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image_file",
                imageFile.name,
                requestImageFile
            )
            try {
                scanCardResult.value = Result.Loading
                val response = scanCardRepository.scanCard(multipartBody)
                if (response.errorCode!!.equals(0)) {
                    scanCardResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                scanCardResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                scanCardResult.value = Result.Error(e.toString())
            }
        }
        return scanCardResult
    }
}