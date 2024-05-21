package com.entre.gethub.data.remote.response.profiles

import com.google.gson.annotations.SerializedName

data class UpdateUserProfileResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
