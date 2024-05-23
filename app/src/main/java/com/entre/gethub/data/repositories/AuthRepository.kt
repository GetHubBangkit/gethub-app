package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class AuthRepository private constructor(private val apiService: ApiService) {

    suspend fun register(fullname: String, email: String, password: String): ApiResponse {
        return apiService.register(fullname, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    companion object {
        fun getInstance(apiService: ApiService): AuthRepository = AuthRepository(apiService)
    }
}