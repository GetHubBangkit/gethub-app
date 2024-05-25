package com.entre.gethub.ui.home.mygethub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.response.products.ProductListResponse
import com.entre.gethub.data.repositories.LinkRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeKelolaMyGethubViewModel(
    private val productRepository: ProductRepository,
    private val linkRepository: LinkRepository
) : ViewModel() {
    private val getProductListResult = MediatorLiveData<Result<ProductListResponse>>()
    private val deleteProductResult = MediatorLiveData<Result<ApiResponse>>()
    private val getLinkResult = MediatorLiveData<Result<LinkResponse>>()
    private val deleteLinkResult = MediatorLiveData<Result<ApiResponse>>()

    fun getProductList(): LiveData<Result<ProductListResponse>> {
        viewModelScope.launch {
            try {
                getProductListResult.value = Result.Loading
                val response = productRepository.getProductList()
                if (response.success == true) {
                    getProductListResult.value = Result.Success(response)
                } else {
                    getProductListResult.value = Result.Empty("Produk masih kosong")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getProductListResult.value = Result.Empty("Produk masih kosong")
                } else {
                    getProductListResult.value = Result.Error(errorMessage!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getProductListResult.value = Result.Error(e.toString())
            }
        }
        return getProductListResult
    }

    fun deleteProduct(id: String): LiveData<Result<ApiResponse>> {
        viewModelScope.launch {
            try {
                deleteProductResult.value = Result.Loading
                val response = productRepository.deleteProduct(id)
                if (response.success == true) {
                    deleteProductResult.value = Result.Success(response)
                    getProductList()
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                deleteProductResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                deleteProductResult.value = Result.Error(e.toString())
            }
        }
        return deleteProductResult
    }

    // Links
    fun getLinks(): LiveData<Result<LinkResponse>> {
        viewModelScope.launch {
            try {
                getLinkResult.value = Result.Loading
                val response = linkRepository.getLinks()
                if (response.success == true) {
                    getLinkResult.value = Result.Success(response)
                } else {
                    getLinkResult.value = Result.Error(response.message ?: "Unknown Error")
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                if (e.code().equals(404)) {
                    getLinkResult.value = Result.Empty("Link masih kosong")
                } else {
                    getLinkResult.value = Result.Error(errorMessage!!)
                }
            } catch (e: Exception) {
                getLinkResult.value = Result.Error(e.message ?: "Error Occurred")
                Log.e(TAG, "getLinks: $e")
            }
        }
        return getLinkResult
    }

    fun deleteLink(linkId: String): LiveData<Result<ApiResponse>> {
        viewModelScope.launch {
            try {
                deleteLinkResult.value = Result.Loading
                val response = linkRepository.deleteLink(linkId)
                if (response.success == true) {
                    deleteLinkResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                deleteLinkResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                deleteLinkResult.value = Result.Error(e.toString())
            }
        }

        return deleteLinkResult
    }

    companion object {
        private const val TAG = "HomeKelolaMyGethubViewModel"
    }
}