package com.entre.gethub.ui.home.mygethub.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.CategoriesResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.products.ProductResponse
import com.entre.gethub.data.repositories.CategoryRepository
import com.entre.gethub.data.repositories.ProductRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class HomeKelolaMyGethubTambahProdukViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
) :
    ViewModel() {

    private val createProductResult = MediatorLiveData<Result<ProductResponse>>()
    private val uploadProductImageResult = MediatorLiveData<Result<UploadFileResponse>>()
    private val getCategoriesResult = MediatorLiveData<Result<CategoriesResponse>>()

    fun addProduct(
        name: String,
        description: String,
        imageUrl: String,
        categoryId: String,
    ): LiveData<Result<ProductResponse>> {
        if (name.isEmpty() || description.isEmpty() ||imageUrl.isEmpty() || categoryId.isEmpty()) {
            createProductResult.value = Result.Error("Semua bidang harus diisi")
            return createProductResult
        }
        viewModelScope.launch {
            try {
                createProductResult.value = Result.Loading
                val response = productRepository.addProduct(name, description, imageUrl, categoryId)
                if (response.success == true) {
                    createProductResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                createProductResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                e.printStackTrace()
                createProductResult.value = Result.Error(e.toString())
            }
        }
        return createProductResult
    }

    fun uploadProfilePhoto(imageFile: File): LiveData<Result<UploadFileResponse>> {
        viewModelScope.launch {
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )
            try {
                uploadProductImageResult.value = Result.Loading
                val response = productRepository.uploadPhoto(multipartBody)
                if (response.success == true) {
                    uploadProductImageResult.value = Result.Success(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uploadProductImageResult.value = Result.Error(e.toString())
            }
        }
        return uploadProductImageResult
    }

    fun getCategories(): LiveData<Result<CategoriesResponse>> {
        viewModelScope.launch {
            try {
                getCategoriesResult.value = Result.Loading
                val response = categoryRepository.getCategories()

                if (response.success == true) {
                    getCategoriesResult.value = Result.Success(response)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ApiResponse::class.java)
                val errorMessage = errorBody.message
                getCategoriesResult.value = Result.Error(errorMessage!!)
            } catch (e: Exception) {
                getCategoriesResult.value = Result.Error(e.toString())
            }
        }
        return getCategoriesResult
    }
}