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

    fun getPartnerList(): LiveData<Result<GetHubPartnerListResponse>> {
        viewModelScope.launch {
            try {
                getHubPartnerListResult.value = Result.Loading
                val response = gethubRepository.getPartnerList()
                if (response.success == true) {
                    getHubPartnerListResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody?.message ?: "Terjadi kesalahan"
                if (e.code() == 404) {
                    getHubPartnerListResult.value = Result.Empty("List Partner Masih Kosong")
                } else {
                    getHubPartnerListResult.value = Result.Error(errorMessage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getHubPartnerListResult.value = Result.Error(e.toString())
            }
        }

        return getHubPartnerListResult
    }

    fun searchPartner(name: String): LiveData<Result<SearchingPartnerResponse>> {
        val searchResult = MediatorLiveData<Result<SearchingPartnerResponse>>()
        viewModelScope.launch {
            try {
                searchResult.value = Result.Loading
                val response = gethubRepository.searchPartner(name)
                if (response.success) {
                    searchResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody?.message ?: "Terjadi kesalahan"
                if (e.code() == 404) {
                    searchResult.value = Result.Empty("List Partner Masih Kosong")
                } else {
                    searchResult.value = Result.Error(errorMessage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                searchResult.value = Result.Error(e.toString())
            }
        }
        return searchResult
    }
}
