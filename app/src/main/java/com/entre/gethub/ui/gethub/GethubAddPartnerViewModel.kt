package com.entre.gethub.ui.gethub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.params.AddPartnerParams
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.ml.ErrorResponse
import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import com.entre.gethub.data.remote.response.partners.AddPartnerResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.GethubRepository
import com.entre.gethub.data.repositories.ScanCardRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class GethubAddPartnerViewModel(
    private val gethubRepository: GethubRepository,
    private val scanCardRepository: ScanCardRepository
) : ViewModel() {

    private val addPartnerResult = MediatorLiveData<Result<AddPartnerResponse>>()
    private val scanCardResult = MediatorLiveData<Result<ScanCardResponse>>()



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
                if (response.errorCode == 0) {
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
