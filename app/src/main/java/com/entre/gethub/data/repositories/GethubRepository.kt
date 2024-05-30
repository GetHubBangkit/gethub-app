package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.params.AddPartnerParams
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.partners.AddPartnerResponse
import com.entre.gethub.data.remote.response.partners.GetHubPartnerListResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class GethubRepository private constructor(private val apiService: ApiService) {
    suspend fun getUserProfile(): UserProfileResponse {
        return apiService.getUserProfile()
    }

    suspend fun addPartner(addPartnerParams: AddPartnerParams): AddPartnerResponse {
        return apiService.addPartner(
            addPartnerParams.fullname,
            addPartnerParams.profession,
            addPartnerParams.email,
            addPartnerParams.phone,
            addPartnerParams.website,
            addPartnerParams.address,
            addPartnerParams.photo,
            addPartnerParams.image,
        )
    }

    suspend fun addPartnerQR(qrCode: String): AddPartnerResponse {
        return apiService.addPartnerQR(qrCode)
    }

    suspend fun getPartnerList(): GetHubPartnerListResponse {
        return apiService.getPartnerList()
    }

    suspend fun uploadPhoto(file: MultipartBody.Part): UploadFileResponse {
        return apiService.uploadFile(file)
    }

    companion object {
        fun getInstance(apiService: ApiService): GethubRepository = GethubRepository(apiService)
    }
}