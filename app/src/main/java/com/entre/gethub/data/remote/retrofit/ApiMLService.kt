package com.entre.gethub.data.remote.retrofit

import com.entre.gethub.data.remote.response.CategoriesResponse
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiMLService {
    // Scan Card
    @Multipart
    @Headers("Accept: application/json")
    @POST("scan-card")
    suspend fun scanCard(
        @Part imageFile: MultipartBody.Part
    ): ScanCardResponse

    // Cari Talent
    @GET("users")
    suspend fun getCariTalent(
        @Query("profession") profession: String
    ): CariTalentResponse

    // Scan Fraud Project
    @Headers("Accept: application/json")
    @POST("/api/scan-fraud-project")
    suspend fun scanFraudProject(
        @Part imageFile: MultipartBody.Part
    ): ProjectDetectorResponse

}