package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.UploadFileResponse

import com.entre.gethub.data.remote.response.certifications.CertificationListResponse
import com.entre.gethub.data.remote.response.certifications.CertificationResponse
import com.entre.gethub.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class CertificationRepository private constructor(private val apiService: ApiService) {

    suspend fun getCertificationList(): CertificationListResponse {
        return apiService.getCertificationList()
    }

    suspend fun addCertification(
        title: String,
        image: String,
        categoryId: String
    ): CertificationResponse {
        return apiService.addCertification(title, image, categoryId)
    }

    suspend fun getCertificationDetail(id: String): CertificationResponse {
        return apiService.getCertificationDetail(id)
    }

    suspend fun editCertification(
        id: String,
        title: String,
        image: String,
        categoryId: String
    ): CertificationResponse {
        return apiService.editCertification(id, title, image, categoryId)
    }

    suspend fun deleteCertification(
        id: String
    ): ApiResponse {
        return apiService.deleteCertification(id)
    }

    suspend fun uploadPhoto(file: MultipartBody.Part): UploadFileResponse {
        return apiService.uploadFile(file)
    }

    companion object {
        fun getInstance(apiService: ApiService): CertificationRepository = CertificationRepository(apiService)
    }
}