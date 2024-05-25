package com.entre.gethub.data.remote.retrofit

import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiMLService {
    // Scan Card
    @Multipart
    @Headers("Accept: application/json")
    @POST("scan-card")
    suspend fun scanCard(
        @Part imageFile: MultipartBody.Part
    ): ScanCardResponse

}