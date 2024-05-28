package com.entre.gethub.ui.home.caritalent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.data.repositories.CariTalentRepository
import kotlinx.coroutines.Dispatchers

class HomeCariTalentViewModel(
    private val cariTalentRepository: CariTalentRepository
) : ViewModel() {

    fun searchCariTalent(profession: String): LiveData<CariTalentResponse> {
        return liveData(Dispatchers.IO) {
            try {
                Log.d("HomeCariTalentViewModel", "Searching for profession: $profession")
                val response = cariTalentRepository.getCariTalent(profession)
                Log.d("HomeCariTalentViewModel", "ViewModel received response: $response")
                emit(response)
            } catch (e: Exception) {
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: ${e.message}")
                emit(CariTalentResponse(data = emptyList()))
            }
        }
    }

    fun getAllTalents(): LiveData<CariTalentResponse> {
        return liveData(Dispatchers.IO) {
            try {
                Log.d("HomeCariTalentViewModel", "Fetching all talents")
                val response = cariTalentRepository.getCariTalent("") // Assuming empty profession returns all data
                Log.d("HomeCariTalentViewModel", "ViewModel received response: $response")
                emit(response)
            } catch (e: Exception) {
                Log.e("HomeCariTalentViewModel", "Error in ViewModel: ${e.message}")
                emit(CariTalentResponse(data = emptyList()))
            }
        }
    }
}
