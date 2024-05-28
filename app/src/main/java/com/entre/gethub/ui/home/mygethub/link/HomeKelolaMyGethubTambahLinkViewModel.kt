package com.entre.gethub.ui.home.mygethub.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.response.products.ProductResponse
import com.entre.gethub.data.repositories.LinkRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeKelolaMyGethubTambahLinkViewModel(private val linkRepository: LinkRepository) :
    ViewModel() {

    private val result = MutableLiveData<Result<LinkResponse>>()
    private val addLinkResult = MediatorLiveData<Result<LinkResponse>>()

    fun addLink(category: String, link: String): LiveData<Result<LinkResponse>> {
        if (category.isEmpty() || link.isEmpty()) {
            addLinkResult.value = Result.Error("Semua bidang harus diisi")
            return addLinkResult
        }
        viewModelScope.launch {
            try {
                result.value = Result.Loading
                val response = linkRepository.addLink(category, link)
                if (response.success == true) {
                    result.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, LinkResponse::class.java)
                val errorMessage = errorBody.message
                result.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                result.value = Result.Error(e.toString())
            }
        }
        return result
    }
}