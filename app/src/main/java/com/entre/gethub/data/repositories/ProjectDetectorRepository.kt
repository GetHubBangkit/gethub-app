package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProjectDetectorRepository private constructor(private val apiMLService: ApiMLService) {

    suspend fun scanFraudProject(contents: String): ProjectDetectorResponse {
        val requestBody = contents.toRequestBody()
        return apiMLService.scanFraudProject(requestBody)
    }

    companion object {
        fun getInstance(apiMLService: ApiMLService): ProjectDetectorRepository =
            ProjectDetectorRepository(apiMLService)
    }
}
