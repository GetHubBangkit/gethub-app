package com.entre.gethub.ui.home.mygethub.certification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.CategoriesResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.certifications.CertificationResponse
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.CertificationRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class HomeKelolaMyGethubEditSertifikasiViewModel(
    private val certificationRepository: CertificationRepository,
    private val categoryRepository: CategoryRepository
) :
    ViewModel() {
    private val getCertificationDetailResult = MediatorLiveData<Result<CertificationResponse>>()
    private val editCertificationResult = MediatorLiveData<Result<CertificationResponse>>()
    private val uploadCertificationImageResult = MediatorLiveData<Result<UploadFileResponse>>()
    private val getCategoriesResult = MediatorLiveData<Result<CategoriesResponse>>()

    fun getCertificationDetail(id: String): LiveData<Result<CertificationResponse>> {
        viewModelScope.launch {
            try {
                getCertificationDetailResult.value = Result.Loading
                val response = certificationRepository.getCertificationDetail(id)
                if (response.success == true) {
                    getCertificationDetailResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getCertificationDetailResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getCertificationDetailResult.value = Result.Error(e.toString())
            }
        }
        return getCertificationDetailResult
    }

    fun editCertification(
        id: String,
        title: String,
        image: String,
        categoryId: String
    ): LiveData<Result<CertificationResponse>> {
        if (title.isEmpty() || image.isEmpty() || categoryId.isEmpty()) {
            editCertificationResult.value = Result.Error("Semua bidang harus diisi")
            return editCertificationResult
        }
        viewModelScope.launch {
            try {
                editCertificationResult.value = Result.Loading
                val response = certificationRepository.editCertification(id, title, image, categoryId)
                if (response.success == true) {
                    editCertificationResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                Log.e(TAG, "HTTP Exception: $e")
                editCertificationResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                editCertificationResult.value = Result.Error(e.toString())
                Log.e(TAG, "Exception: $e")
            }
        }
        return editCertificationResult
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
                uploadCertificationImageResult.value = Result.Loading
                val response = certificationRepository.uploadPhoto(multipartBody)
                if (response.success == true) {
                    uploadCertificationImageResult.value = Result.Success(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uploadCertificationImageResult.value = Result.Error(e.toString())
            }
        }
        return uploadCertificationImageResult
    }

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

    companion object {
        const val TAG = "HomeKelolaMyGetHubEditSertifikasiViewModel"
    }
}