package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService

class CariTalentRepository private constructor(private val apiMLService: ApiMLService) {

    suspend fun getCariTalent(profession: String): CariTalentResponse {
        return apiMLService.getCariTalent(profession)
    }

    companion object {
        private var instance: CariTalentRepository? = null

        fun getInstance(apiMLService: ApiMLService): CariTalentRepository {
            return instance ?: synchronized(this) {
                instance ?: CariTalentRepository(apiMLService).also { instance = it }
            }
        }
    }
}
