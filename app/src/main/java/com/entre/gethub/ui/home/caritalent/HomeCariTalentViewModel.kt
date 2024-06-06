package com.entre.gethub.ui.home.caritalent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.data.repositories.CariTalentRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeCariTalentViewModel(
    private val cariTalentRepository: CariTalentRepository
) : ViewModel() {

    private val _searchCariTalentResult = MediatorLiveData<Result<CariTalentResponse>>()
    val searchCariTalentResult: LiveData<Result<CariTalentResponse>> get() = _searchCariTalentResult

    private val _allTalentsResult = MediatorLiveData<Result<CariTalentResponse>>()
    val allTalentsResult: LiveData<Result<CariTalentResponse>> get() = _allTalentsResult

    fun searchCariTalent(profession: String): LiveData<Result<CariTalentResponse>> {
        _searchCariTalentResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HomeCariTalentViewModel", "Searching for profession: $profession")
                val response = cariTalentRepository.getCariTalent(profession)
                if (response.data.isNullOrEmpty()) {
                    _searchCariTalentResult.postValue(Result.Empty("No talents found"))
                } else {
                    _searchCariTalentResult.postValue(Result.Success(response))
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _searchCariTalentResult.postValue(Result.Error(errorMessage))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _searchCariTalentResult.postValue(Result.Error(errorMessage))
            }
        }
        return searchCariTalentResult
    }

    fun getAllTalents(): LiveData<Result<CariTalentResponse>> {
        _allTalentsResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HomeCariTalentViewModel", "Fetching all talents")
                val response = cariTalentRepository.getCariTalent("")
                if (response.data.isNullOrEmpty()) {
                    _allTalentsResult.postValue(Result.Empty("No talents found"))
                } else {
                    _allTalentsResult.postValue(Result.Success(response))
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _allTalentsResult.postValue(Result.Error(errorMessage))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unknown error occurred"
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: $errorMessage")
                _allTalentsResult.postValue(Result.Error(errorMessage))
            }
        }
        return allTalentsResult
    }
}
