package com.entre.gethub.data.remote.response.products

import com.google.gson.annotations.SerializedName

data class ProductResponse(

    @field:SerializedName("data")
	val data: Product? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("error_code")
	val errorCode: Int? = null,

    @field:SerializedName("message")
	val message: String? = null
)