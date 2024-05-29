// UserPublicProfileRepository.kt
package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.UserPublicProfileResponse

class UserPublicProfileRepository(private val apiService: ApiService) {

    suspend fun getPublicProfile(username: String): UserPublicProfileResponse {
        return apiService.getPublicProfile(username)
    }

    companion object {
        // Singleton pattern untuk membuat instance dari repository
        fun getInstance(apiService: ApiService): UserPublicProfileRepository {
            return UserPublicProfileRepository(apiService)
        }
    }
}
