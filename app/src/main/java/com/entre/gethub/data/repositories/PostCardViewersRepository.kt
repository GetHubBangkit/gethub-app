package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.PostCardViewersResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class PostCardViewersRepository(private val apiService: ApiService) {
    suspend fun postCardViewers(username: String): PostCardViewersResponse {
        return apiService.postCardViewers(username)
    }

    companion object {
        fun getInstance(apiService: ApiService): PostCardViewersRepository {
            return PostCardViewersRepository(apiService)
        }
    }
}
