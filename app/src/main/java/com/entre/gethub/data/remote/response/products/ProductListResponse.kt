package com.entre.gethub.data.remote.response.products

import com.google.gson.annotations.SerializedName

data class ProductListResponse(

    @field:SerializedName("data")
	val data: List<Product>,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("error_code")
	val errorCode: Int? = null,

    @field:SerializedName("message")
	val message: String? = null
)

data class Product(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
