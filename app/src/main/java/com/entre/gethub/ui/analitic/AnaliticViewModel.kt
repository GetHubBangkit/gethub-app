package com.entre.gethub.ui.analitic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.AnaliticTotalResponse
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.repositories.AnaliticTotalRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AnaliticViewModel(
    private val analiticTotalRepository: AnaliticTotalRepository
) : ViewModel() {
    private val getAnaliticTotalResult = MediatorLiveData<Result<AnaliticTotalResponse>>()

    fun getAnaliticTotal(): LiveData<Result<AnaliticTotalResponse>> {
        viewModelScope.launch {
            getAnaliticTotalResult.value = Result.Loading
            try {
                val response = analiticTotalRepository.getAnaliticTotal()
                if (response.success == true) {
                    getAnaliticTotalResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getAnaliticTotalResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getAnaliticTotalResult.value = Result.Error(e.message.toString())
            }
        }
        return getAnaliticTotalResult
    }
}