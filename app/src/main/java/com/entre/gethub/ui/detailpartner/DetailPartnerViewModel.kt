package com.entre.gethub.ui.detailpartner


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.SearchingPartnerResponse
import com.entre.gethub.data.remote.response.partners.GetHubPartnerListResponse
import com.entre.gethub.data.repositories.GethubRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailPartnerViewModel(private val gethubRepository: GethubRepository) : ViewModel() {

    private val getHubPartnerListResult = MediatorLiveData<Result<GetHubPartnerListResponse>>()


    fun getPartnerList(): LiveData<Result<GetHubPartnerListResponse>> {
        val resultLiveData = MutableLiveData<Result<GetHubPartnerListResponse>>()
        viewModelScope.launch {
            resultLiveData.value = Result.Loading
            try {
                val response = gethubRepository.getPartnerList()
                if (response.success == true) {
                    resultLiveData.value = Result.Success(response)
                } else {
                    resultLiveData.value = Result.Error("Failed to load partner list")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                resultLiveData.value = Result.Error(errorMessage ?: "An error occurred")
            } catch (e: Exception) {
                e.printStackTrace()
                resultLiveData.value = Result.Error(e.message ?: "An error occurred")
            }
        }
        return resultLiveData
    }



}
