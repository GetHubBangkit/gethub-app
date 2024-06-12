package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService

class InformationHubRepository(private val apiService: ApiService) {

    suspend fun getInformationHub() = apiService.getInformationHub()

    companion object {
        fun getInstance(apiService: ApiService): InformationHubRepository =
            InformationHubRepository(apiService)
    }
}
