package com.entre.gethub.ui.gethub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

class GethubPartnerListViewModel(private val gethubRepository: GethubRepository) : ViewModel() {

    private val getHubPartnerListResult = MediatorLiveData<Result<GetHubPartnerListResponse>>()
    private val searchPartnerResult = MediatorLiveData<Result<SearchingPartnerResponse>>()

    fun getPartnerList(): LiveData<Result<GetHubPartnerListResponse>> {
        viewModelScope.launch {
            try {
                getHubPartnerListResult.value = Result.Loading
                val response = gethubRepository.getPartnerList()
                if (response.success == true) {
                    getHubPartnerListResult.value = Result.Success(response)
                } else {
                    getHubPartnerListResult.value = Result.Error("Failed to load partner list")
                }
            } catch (e: HttpException) {
                handleHttpException(e, getHubPartnerListResult)
            } catch (e: Exception) {
                e.printStackTrace()
                getHubPartnerListResult.value = Result.Error(e.toString())
            }
        }
        return getHubPartnerListResult
    }

    fun searchPartner(name: String?, profession: String?): LiveData<Result<SearchingPartnerResponse>> {
        viewModelScope.launch {
            try {
                searchPartnerResult.value = Result.Loading
                val response = gethubRepository.searchPartner(name, profession)
                if (response.success) {
                    searchPartnerResult.value = Result.Success(response)
                } else {
                    searchPartnerResult.value = Result.Error("Failed to search partners")
                }
            } catch (e: HttpException) {
                handleHttpException(e, searchPartnerResult)
            } catch (e: Exception) {
                e.printStackTrace()
                searchPartnerResult.value = Result.Error(e.toString())
            }
        }
        return searchPartnerResult
    }

    private fun <T> handleHttpException(e: HttpException, liveData: MediatorLiveData<Result<T>>) {
        val jsonString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
        val errorMessage = errorBody?.message ?: "Terjadi kesalahan"
        if (e.code() == 404) {
            liveData.value = Result.Empty("List Partner Masih Kosong")
        } else {
            liveData.value = Result.Error(errorMessage)
        }
    }
}
