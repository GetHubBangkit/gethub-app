package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.data.remote.response.products.ProductListResponse

class UserPublicProfileRepository(private val apiService: ApiService) {

    suspend fun getPublicProfile(username: String): UserPublicProfileResponse {
        return apiService.getPublicProfile(username)
    }

    suspend fun getLinks(username: String): List<UserPublicProfileResponse.Data.Link> {
        val userProfileResponse = apiService.getPublicProfile(username)
        return userProfileResponse.data?.links ?: emptyList()
    }

    suspend fun getProducts(username: String): List<UserPublicProfileResponse.Data.Product> {
        val userProfileResponse = apiService.getPublicProfile(username)
        return userProfileResponse.data?.products ?: emptyList()
    }

    suspend fun getCertifications(username: String): List<UserPublicProfileResponse.Data.Certifications> {
        val userProfileResponse = apiService.getPublicProfile(username)
        return userProfileResponse.data?.certifications ?: emptyList()
    }
    suspend fun getProjects(username: String): List<UserPublicProfileResponse.Data.Projects> {
        val userProfileResponse = apiService.getPublicProfile(username)
        return userProfileResponse.data?.projects ?: emptyList()
    }



    companion object {
        fun getInstance(apiService: ApiService): UserPublicProfileRepository {
            return UserPublicProfileRepository(apiService)
        }
    }
}
