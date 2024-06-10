package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ml.ScanKTPResponse
import com.entre.gethub.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class ScanKTPRepository(private val apiService: ApiService) {

    suspend fun scanKTP(imageFile: MultipartBody.Part): ScanKTPResponse {
        return apiService.scanKTP(imageFile)
    }

    companion object {
        fun getInstance(apiService: ApiService): ScanKTPRepository =
            ScanKTPRepository(apiService)
    }
}
