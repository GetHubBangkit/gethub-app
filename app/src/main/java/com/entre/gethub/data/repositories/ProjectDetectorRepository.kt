package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService
import okhttp3.MultipartBody

class ProjectDetectorRepository(private val apiMLService: ApiMLService) {

    suspend fun scanFraudProject(imageFile: MultipartBody.Part): ProjectDetectorResponse {
        return apiMLService.scanFraudProject(imageFile)
    }

    companion object {
        fun getInstance(apiMLService: ApiMLService): ProjectDetectorRepository =
            ProjectDetectorRepository(apiMLService)
    }
}
