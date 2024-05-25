package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.CategoriesResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class CategoryRepository private constructor(private val apiService: ApiService){

    suspend fun getCategories(): CategoriesResponse {
        return apiService.getCategories()
    }

    companion object {
        fun getInstance(apiService: ApiService) = CategoryRepository(apiService)
    }
}