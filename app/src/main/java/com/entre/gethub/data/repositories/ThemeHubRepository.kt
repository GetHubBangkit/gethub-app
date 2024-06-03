package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.retrofit.ApiService
import com.entre.gethub.data.remote.response.ThemeHubResponse

class ThemeHubRepository(private val apiService: ApiService) {
    suspend fun updateThemeHub(themeHub: Int): ThemeHubResponse {
        return apiService.updateThemeHub(themeHub)
    }

    companion object {
        fun getInstance(apiService: ApiService): ThemeHubRepository {
            return ThemeHubRepository(apiService)
        }
    }
}
