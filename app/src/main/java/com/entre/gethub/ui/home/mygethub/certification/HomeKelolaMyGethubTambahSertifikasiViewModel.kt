package com.entre.gethub.ui.home.mygethub.certification

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

class HomeKelolaMyGethubTambahSertifikasiViewModel(
    private val certificationRepository: CertificationRepository,
    private val categoryRepository: CategoryRepository,
) :
    ViewModel() {

    private val createCertificationResult = MediatorLiveData<Result<CertificationResponse>>()
    private val uploadCertificationImageResult = MediatorLiveData<Result<UploadFileResponse>>()
    private val getCategoriesResult = MediatorLiveData<Result<CategoriesResponse>>()

    fun addCertification(
        title: String,
        image: String,
        categoryId: String,
    ): LiveData<Result<CertificationResponse>> {
        viewModelScope.launch {
            try {
                createCertificationResult.value = Result.Loading
                val response = certificationRepository.addCertification(title, image, categoryId)
                if (response.success == true) {
                    createCertificationResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                createCertificationResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                createCertificationResult.value = Result.Error(e.toString())
            }
        }
        return createCertificationResult
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
}