package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.params.UpdateUserProfileParams
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.profiles.UpdateUserProfileResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import okhttp3.MultipartBody

class ProfileRepository private constructor(private val apiService: ApiService) {
    suspend fun getUserProfile(): UserProfileResponse {
        return apiService.getUserProfile()
    }

    suspend fun updateUserProfile(
        updateUserProfileParams: UpdateUserProfileParams
    ): UpdateUserProfileResponse {
        return apiService.updateUserProfile(
            updateUserProfileParams.fullname,
            updateUserProfileParams.profession,
            updateUserProfileParams.email,
            updateUserProfileParams.phone,
            updateUserProfileParams.web,
            updateUserProfileParams.address,
            updateUserProfileParams.about,
            updateUserProfileParams.photo ?: null
        )
    }

    suspend fun uploadPhoto(file: MultipartBody.Part): UploadFileResponse {
        return apiService.uploadFile(file)
    }

    companion object {
        fun getInstance(apiService: ApiService): ProfileRepository =
            ProfileRepository(apiService)
    }
}