package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.CardViewersResponse

class CardViewersRepository(private val apiService: ApiService) {
    suspend fun getCardViewers(): CardViewersResponse {
        return apiService.getCardViewers()
    }

    companion object {
        fun getInstance(apiService: ApiService): CardViewersRepository {
            return CardViewersRepository(apiService)
        }
    }
}
