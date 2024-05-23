package com.entre.gethub.data.remote.response.partners

import com.google.gson.annotations.SerializedName

data class AddPartnerResponse(

    @field:SerializedName("data")
	val data: Partner? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("error_code")
	val errorCode: Int? = null,

    @field:SerializedName("message")
	val message: String? = null
) {

	data class Partner(

		@field:SerializedName("profession")
		val profession: String? = null,

		@field:SerializedName("createdAt")
		val createdAt: String? = null,

		@field:SerializedName("website")
		val website: String? = null,

		@field:SerializedName("full_name")
		val fullName: String? = null,

		@field:SerializedName("address")
		val address: String? = null,

		@field:SerializedName("phone")
		val phone: String? = null,

		@field:SerializedName("user_id")
		val userId: String? = null,

		@field:SerializedName("photo")
		val photo: String? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("email")
		val email: String? = null,

		@field:SerializedName("updatedAt")
		val updatedAt: String? = null
	)

}