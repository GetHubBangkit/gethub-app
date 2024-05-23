package com.entre.gethub.data.remote.response.partners

import com.google.gson.annotations.SerializedName

data class GetHubPartnerListResponse(

    @field:SerializedName("data")
	val data: List<GetHubPartner>,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("error_code")
	val errorCode: Int? = null,

    @field:SerializedName("message")
	val message: String? = null
)

data class GetHubPartner(

	@field:SerializedName("profession")
	val profession: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("ref_user_id")
	val refUserId: Any? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: Any? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
