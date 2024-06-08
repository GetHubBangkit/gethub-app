package com.entre.gethub.data.repositories

import android.util.Log
import com.entre.gethub.data.remote.response.ReysEventResponse
import com.entre.gethub.data.remote.retrofit.ApiMLService

class ReysEventRepository(private val apiMLService: ApiMLService) {

    suspend fun getReysEvent(profession: String): ReysEventResponse {
        val response = apiMLService.getReysEvent(profession)
        return response
    }

    companion object {
        fun getInstance(apiService: ApiMLService): ReysEventRepository =
            ReysEventRepository(apiService)
    }
}
