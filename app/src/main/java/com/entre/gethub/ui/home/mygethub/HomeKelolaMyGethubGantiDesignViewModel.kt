package com.entre.gethub.ui.home.mygethub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.response.ThemeHubResponse
import com.entre.gethub.data.repositories.CariTalentRepository
import com.entre.gethub.data.repositories.ThemeHubRepository
import com.entre.gethub.di.Injection // Import your Injection class
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeKelolaMyGethubGantiDesignViewModel(
    private val themeHubRepository: ThemeHubRepository
) : ViewModel() {

    private val result = MutableLiveData<Result<ThemeHubResponse>>()
    private val updateThemeHubResult = MediatorLiveData<Result<ThemeHubResponse>>()

    fun updateThemeHub(themeHub: Int) {
        viewModelScope.launch {
            updateThemeHubResult.value = Result.Loading
            try {
                val response = themeHubRepository.updateThemeHub(themeHub)
                if (response.success == true) { // Use safe call operator to handle nullable Boolean
                    updateThemeHubResult.value = Result.Success(response)
                } else {
                    updateThemeHubResult.value = Result.Error(response.message ?: "Unknown error") // Elvis operator to provide a default message
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ThemeHubResponse::class.java)
                val errorMessage = errorBody?.message ?: "Unknown error" // Elvis operator to provide a default message
                updateThemeHubResult.value = Result.Error(errorMessage)
            } catch (e: Exception) {
                updateThemeHubResult.value = Result.Error(e.toString())
            }
        }
    }
}
