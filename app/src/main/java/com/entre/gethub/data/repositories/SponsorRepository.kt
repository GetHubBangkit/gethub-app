package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.SponsorResponse

class SponsorRepository(private val apiService: ApiService) {
    suspend fun getSponsors(): SponsorResponse {
        return apiService.getSponsors()
    }

    companion object {
        fun getInstance(apiService: ApiService): SponsorRepository {
            return SponsorRepository(apiService)
        }
    }
}
