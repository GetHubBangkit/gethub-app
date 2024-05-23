package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService

class InformationHubRepository(private val apiService: ApiService) {

    suspend fun getInformationHub() = apiService.getInformationHub()

    companion object {
        @Volatile
        private var instance: InformationHubRepository? = null

        fun getInstance(apiService: ApiService): InformationHubRepository =
            instance ?: synchronized(this) {
                instance ?: InformationHubRepository(apiService).also { instance = it }
            }
    }
}
