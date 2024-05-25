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

