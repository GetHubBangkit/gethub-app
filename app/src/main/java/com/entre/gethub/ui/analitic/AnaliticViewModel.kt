package com.entre.gethub.ui.analitic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.AnaliticTotalResponse
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.repositories.AnaliticTotalRepository
import com.entre.gethub.data.remote.response.CardViewersResponse
import com.entre.gethub.data.remote.response.GraphDataResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.CardViewersRepository
import com.entre.gethub.data.repositories.GraphDataRepository
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.data.repositories.VisibilityRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AnaliticViewModel(
    private val analiticTotalRepository: AnaliticTotalRepository,
    private val cardViewersRepository: CardViewersRepository,
    private val graphDataRepository: GraphDataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val getAnaliticTotalResult = MediatorLiveData<Result<AnaliticTotalResponse>>()
    private val getcardViewersTotalResult = MediatorLiveData<Result<CardViewersResponse>>()
    private val getGraphDataResult = MediatorLiveData<Result<GraphDataResponse>>()
    private val getUserProfileResult = MediatorLiveData<Result<UserProfileResponse>>()

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

    fun getCardViewers(): LiveData<Result<CardViewersResponse>> {
        viewModelScope.launch {
            getcardViewersTotalResult.value = Result.Loading
            try {
                val response = cardViewersRepository.getCardViewers()
                if (response.success == true) {
                    getcardViewersTotalResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getcardViewersTotalResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getAnaliticTotalResult.value = Result.Error(e.message.toString())
            }
        }
        return getcardViewersTotalResult
    }

    fun getGraphData(): LiveData<Result<GraphDataResponse>> {
        viewModelScope.launch {
            getGraphDataResult.value = Result.Loading
            try {
                val response = graphDataRepository.getGraphData()
                if (response.success == true) {
                    getGraphDataResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getGraphDataResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                getGraphDataResult.value = Result.Error(e.message.toString())
            }
        }
        return getGraphDataResult
    }

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        viewModelScope.launch {
            getUserProfileResult.value = Result.Loading
            try {
                val response = profileRepository.getUserProfile()
                if (response.success == true) {
                    getUserProfileResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getUserProfileResult.value = Result.Error(errorMessage ?: "An error occurred")
            } catch (e: Exception) {
                getUserProfileResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }
        return getUserProfileResult
    }
}