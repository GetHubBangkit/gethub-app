package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class LinkRepository(private val apiService: ApiService) {

    suspend fun addLink(category: String, link: String): LinkResponse {
        return apiService.addLink(category, link)
    }

    suspend fun getLinks(): LinkResponse {
        return apiService.getLinks()
    }

    suspend fun deleteLink(linkId: String): ApiResponse {
        return apiService.deleteLink(linkId)
    }

    companion object {
        fun getInstance(apiService: ApiService): LinkRepository {
            return LinkRepository(apiService)
        }
    }
}
