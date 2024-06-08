package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.premium.PremiumResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class PremiumRepository private constructor(private val apiService: ApiService){

    suspend fun premium(): PremiumResponse {
        return apiService.premium()
    }


    companion object {
        fun getInstance(apiService: ApiService): PremiumRepository = PremiumRepository(apiService)
    }
}