package com.entre.gethub.ui.akun.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.premium.PremiumResponse
import com.entre.gethub.data.repositories.PremiumRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MembershipViewModel(private val premiumRepository: PremiumRepository) : ViewModel() {
    private val premiumResult = MediatorLiveData<Result<PremiumResponse>>()

    fun premium(): LiveData<Result<PremiumResponse>> {
        viewModelScope.launch {
            premiumResult.value = Result.Loading
            try {
                val response = premiumRepository.premium()
                if (response.success == true) {
                    premiumResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                premiumResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                premiumResult.value = Result.Error(e.toString())
            }
        }
        return premiumResult
    }
}