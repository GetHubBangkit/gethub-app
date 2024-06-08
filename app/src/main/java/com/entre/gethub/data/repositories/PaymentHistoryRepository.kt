package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.premium.PaymentHistoryResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class PaymentHistoryRepository private constructor(private val apiService: ApiService){

    suspend fun getPaymentHistory(): PaymentHistoryResponse {
        return apiService.getPaymentHistory()
    }

    companion object {
        fun getInstance(apiService: ApiService): PaymentHistoryRepository = PaymentHistoryRepository(apiService)
    }
}