package com.bangkit.gethub.data.remote.retrofit

import com.bangkit.gethub.data.remote.response.ApiResponse
import com.bangkit.gethub.data.remote.response.auth.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}