package com.entre.gethub.data.remote.retrofit

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.InformationHubResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.data.remote.response.profiles.UpdateUserProfileResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    // Auth
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse
    // Auth

    // Complete Profile
    @GET("profile")
    suspend fun getUserProfile(): UserProfileResponse

    @FormUrlEncoded
    @PUT("profile")
    suspend fun updateUserProfile(
        @Field("full_name") fullname: String,
        @Field("profession") profession: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("web") web: String,
        @Field("address") address: String,
        @Field("photo") photo: String?,
    ): UpdateUserProfileResponse
    // Complete Profile

    // Upload File
    @Multipart
    @Headers("Accept: application/json")
    @POST("upload-file")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): UploadFileResponse
    // Upload File

    // InformationHub
    @GET("informations")
    suspend fun getInformationHub(): InformationHubResponse
}