package com.entre.gethub.ui.gethub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.params.AddPartnerParams
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.partners.AddPartnerResponse
import com.entre.gethub.data.repositories.GethubRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class GethubAddPartnerFormViewModel(private val gethubRepository: GethubRepository) : ViewModel() {
    private val addPartnerResult = MediatorLiveData<Result<AddPartnerResponse>>()
    private val uploadProfilePhotoResult = MediatorLiveData<Result<UploadFileResponse>>()
    private val addPartnerQRResult = MediatorLiveData<Result<AddPartnerResponse>>()

    fun addPartner(addPartnerParams: AddPartnerParams): LiveData<Result<AddPartnerResponse>> {
        viewModelScope.launch {
            try {
                addPartnerResult.value = Result.Loading
                val response = gethubRepository.addPartner(addPartnerParams)
                if (response.success == true) {
                    addPartnerResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                addPartnerResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                addPartnerResult.value = Result.Error(e.message.toString())
            }
        }
        return addPartnerResult
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
                val response = gethubRepository.uploadPhoto(multipartBody)
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

    fun addPartnerQR(qrCode: String): LiveData<Result<AddPartnerResponse>> {
        viewModelScope.launch {
            try {
                addPartnerQRResult.value = Result.Loading
                val response = gethubRepository.addPartnerQR(qrCode)
                if (response.success == true) {
                    addPartnerQRResult.value = Result.Success(response)
                } else {
                    addPartnerQRResult.value = Result.Error(response.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                addPartnerQRResult.value = Result.Error(e.message ?: "Exception occurred")
            }
        }
        return addPartnerQRResult
    }
}