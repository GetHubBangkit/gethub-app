package com.bangkit.gethub.data.repositories

import com.bangkit.gethub.data.remote.response.ApiResponse
import com.bangkit.gethub.data.remote.retrofit.ApiService

class AuthRepository private constructor(private val apiService: ApiService) {

    suspend fun register(fullname: String, email: String, password: String): ApiResponse {
        return apiService.register(fullname, email, password)
    }

    companion object {
        fun getInstance(apiService: ApiService): AuthRepository = AuthRepository(apiService)
    }
}