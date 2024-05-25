package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService
import okhttp3.MultipartBody

class ScanCardRepository private constructor(private val apiMLService: ApiMLService) {

    suspend fun scanCard(imageFile: MultipartBody.Part): ScanCardResponse {
        return apiMLService.scanCard(imageFile)
    }

    companion object {
        fun getInstance(apiMLService: ApiMLService): ScanCardRepository =
            ScanCardRepository(apiMLService)
    }
}