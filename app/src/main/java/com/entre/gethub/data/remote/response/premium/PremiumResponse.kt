package com.entre.gethub.data.remote.response.premium

import com.google.gson.annotations.SerializedName

data class PremiumResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error_code")
	val errorCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("redirect_url")
	val redirectUrl: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
