package com.entre.gethub.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.profiles.InformationHubResponse
import com.entre.gethub.data.repositories.InformationHubRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val informationHubRepository: InformationHubRepository,
) : ViewModel() {

    private val _informationHubs = MutableLiveData<Result<List<InformationHubResponse.Data>>>(Result.Loading)
    val informationHubs: LiveData<Result<List<InformationHubResponse.Data>>> get() = _informationHubs


    init {
        getInformationHubs()
    }



    fun getInformationHubs() {
        viewModelScope.launch {
            _informationHubs.value = Result.Loading
            try {
                val response = informationHubRepository.getInformationHub()
                if (response.success == true) {
                    _informationHubs.value = Result.Success(response.data ?: emptyList())
                } else {
                    _informationHubs.value = Result.Error(response.message ?: "Unknown Error")
                }
            } catch (e: Exception) {
                _informationHubs.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
    }


}
