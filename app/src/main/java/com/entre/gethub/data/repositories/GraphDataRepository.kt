package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.GraphDataResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class GraphDataRepository(private val apiService: ApiService) {

    suspend fun getGraphData(): GraphDataResponse {
        return apiService.getGraphData()
    }

    companion object {
        fun getInstance(apiService: ApiService): GraphDataRepository {
            return GraphDataRepository(apiService)
        }
    }
}
