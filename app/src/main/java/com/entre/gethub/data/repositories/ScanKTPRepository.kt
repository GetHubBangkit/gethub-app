package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ml.ScanKTPResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService
import okhttp3.MultipartBody

class ScanKTPRepository(private val apiMLService: ApiMLService) {

    suspend fun scanFraudProject(imageFile: MultipartBody.Part): ScanKTPResponse {
        return apiMLService.scanKTP(imageFile)
    }

    companion object {
        fun getInstance(apiMLService: ApiMLService): ScanKTPRepository =
            ScanKTPRepository(apiMLService)
    }
}
