package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

    @field:SerializedName("data")
    val data: List<Category> = emptyList(),

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Category(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)
