package com.entre.gethub.data.repositories

import android.util.Log
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService

class CariTalentRepository(private val apiMLService: ApiMLService) {

    suspend fun getCariTalent(profession: String): CariTalentResponse {
        Log.d("CariTalentRepository", "Fetching talent for profession: $profession")
        val response = apiMLService.getCariTalent(profession)
        Log.d("CariTalentRepository", "API Response: $response")
        return response
    }

    companion object {
        fun getInstance(apiMLService: ApiMLService): CariTalentRepository =
            CariTalentRepository(apiMLService)
    }
}
