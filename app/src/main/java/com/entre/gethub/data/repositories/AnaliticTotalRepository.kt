package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.AnaliticTotalResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class AnaliticTotalRepository(private val apiService: ApiService) {
    suspend fun getAnaliticTotal(): AnaliticTotalResponse {
        return apiService.getAnaliticTotal()
    }

    companion object {
        fun getInstance(apiService: ApiService): AnaliticTotalRepository {
            return AnaliticTotalRepository(apiService)
        }
    }
}
