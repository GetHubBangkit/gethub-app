package com.entre.gethub.data.remote.response.profiles

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    data class Data(

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("web")
        val web: String? = null,

        @field:SerializedName("about")
        val about: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}
