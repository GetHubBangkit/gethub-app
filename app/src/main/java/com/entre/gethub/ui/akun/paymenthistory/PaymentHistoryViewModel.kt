package com.entre.gethub.ui.akun.paymenthistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.premium.PaymentHistoryResponse
import com.entre.gethub.data.repositories.PaymentHistoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PaymentHistoryViewModel(private val paymentHistoryRepository: PaymentHistoryRepository) :
    ViewModel() {
    private val getPaymentHistoryResult = MediatorLiveData<Result<PaymentHistoryResponse>>()

    fun getPaymentHistory(): LiveData<Result<PaymentHistoryResponse>> {
        viewModelScope.launch {
            getPaymentHistoryResult.value = Result.Loading
            try {
                val response = paymentHistoryRepository.getPaymentHistory()
                if (response.success == true) {
                    getPaymentHistoryResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getPaymentHistoryResult.value = Result.Empty(errorMessage!!)
                }
                getPaymentHistoryResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getPaymentHistoryResult.value = Result.Error(e.toString())
            }
        }
        return getPaymentHistoryResult
    }
}