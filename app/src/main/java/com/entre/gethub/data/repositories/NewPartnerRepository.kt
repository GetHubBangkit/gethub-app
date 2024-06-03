package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.NewPartnerResponse

class NewPartnerRepository(private val apiService: ApiService) {
    suspend fun getNewPartner(): NewPartnerResponse {
        return apiService.getNewPartner()
    }

    companion object {
        fun getInstance(apiService: ApiService): NewPartnerRepository {
            return NewPartnerRepository(apiService)
        }
    }
}
