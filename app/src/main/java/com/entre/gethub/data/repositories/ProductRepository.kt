package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.products.ProductListResponse
import com.entre.gethub.data.remote.response.products.ProductResponse
import com.entre.gethub.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class ProductRepository private constructor(private val apiService: ApiService) {

    suspend fun getProductList(): ProductListResponse {
        return apiService.getProductList()
    }

    suspend fun addProduct(
        name: String,
        description: String,
        imageUrl: String
    ): ProductResponse {
        return apiService.addProduct(name, description, imageUrl)
    }

    suspend fun getProductDetail(id: String): ProductResponse {
        return apiService.getProductDetail(id)
    }

    suspend fun editProduct(
        id: String,
        name: String,
        description: String,
        imageUrl: String
    ): ProductResponse {
        return apiService.editProduct(id, name, description, imageUrl)
    }

    suspend fun deleteProduct(
        id: String
    ): ApiResponse {
        return apiService.deleteProduct(id)
    }

    suspend fun uploadPhoto(file: MultipartBody.Part): UploadFileResponse {
        return apiService.uploadFile(file)
    }

    companion object {
        fun getInstance(apiService: ApiService): ProductRepository = ProductRepository(apiService)
    }
}