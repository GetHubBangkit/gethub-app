package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.TopTalentResponse

class TopTalentRepository(private val apiService: ApiService) {
    suspend fun getTopTalent(): TopTalentResponse {
        return apiService.getTopTalent()
    }

    companion object {
        fun getInstance(apiService: ApiService): TopTalentRepository {
            return TopTalentRepository(apiService)
        }
    }
}
