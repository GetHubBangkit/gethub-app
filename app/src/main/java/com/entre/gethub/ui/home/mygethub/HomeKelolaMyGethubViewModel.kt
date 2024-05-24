package com.entre.gethub.ui.home.mygethub

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

class HomeKelolaMyGethubViewModel(private val productRepository: ProductRepository, private val linkRepository: LinkRepository) : ViewModel() {
    private val getProductListResult = MediatorLiveData<Result<ProductListResponse>>()
    private val deleteProductResult = MediatorLiveData<Result<ApiResponse>>()

    init {
        getLinks()
    }

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
                getProductListResult.value = Result.Error(errorMessage!!)
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

    private val _links = MutableLiveData<Result<List<LinkResponse.Data>>>(Result.Loading)
    val links: LiveData<Result<List<LinkResponse.Data>>> get() = _links
    fun getLinks() {
        viewModelScope.launch {
            _links.value = Result.Loading
            try {
                val response = linkRepository.getLinks()
                if (response.success == true) {
                    _links.value = Result.Success(response.data ?: emptyList())
                } else {
                    _links.value = Result.Error(response.message ?: "Unknown Error")
                }
            } catch (e: Exception) {
                _links.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
    }

    fun addLink(category: String, link: String) {
        viewModelScope.launch {
            try {
                linkRepository.addLink(category, link)
                getLinks() // Refresh links after adding a new one
            } catch (e: Exception) {
                _links.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
    }

    fun deleteLink(linkId: String) {
        viewModelScope.launch {
            try {
                linkRepository.deleteLink(linkId)
                getLinks() // Refresh links after deleting
            } catch (e: Exception) {
                _links.value = Result.Error(e.message ?: "Error Occurred")
            }
        }
    }
}