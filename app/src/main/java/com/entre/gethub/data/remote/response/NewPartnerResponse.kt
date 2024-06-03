package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewPartnerResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val data: List<Data>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null
) {
    data class Data(
        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("user_id")
        val userId: String? = null,

        @field:SerializedName("ref_user_id")
        val refUserId: String? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("website")
        val website: String? = null,

        @field:SerializedName("image_url")
        val imageUrl: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}
