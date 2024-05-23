package com.entre.gethub.ui.completeprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.params.UpdateUserProfileParams
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.remote.response.profiles.UpdateUserProfileResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class CompleteProfileViewModel(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()
    private val updateUserProfileResult = MediatorLiveData<Result<UpdateUserProfileResponse>>()
    private val uploadProfilePhotoResult = MediatorLiveData<Result<UploadFileResponse>>()

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

    fun updateUserProfile(
        updateUserProfileParams: UpdateUserProfileParams
    ): LiveData<Result<UpdateUserProfileResponse>> {
        viewModelScope.launch {
            updateUserProfileResult.value = Result.Loading
            try {
                val response = profileRepository.updateUserProfile(
                    updateUserProfileParams
                )
                if (response.success == true) {
                    updateUserProfileResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                updateUserProfileResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                updateUserProfileResult.value = Result.Error(e.message.toString())
            }
        }
        return updateUserProfileResult
    }

    fun uploadProfilePhoto(imageFile: File): LiveData<Result<UploadFileResponse>> {
        viewModelScope.launch {
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )
            try {
                uploadProfilePhotoResult.value = Result.Loading
                val response = profileRepository.uploadPhoto(multipartBody)
                if (response.success == true) {
                    uploadProfilePhotoResult.value = Result.Success(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uploadProfilePhotoResult.value = Result.Error(e.toString())
            }
        }
        return uploadProfilePhotoResult
    }
}