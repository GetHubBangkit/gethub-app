package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class CardViewersResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val data: List<DataItem>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null
) {
    data class DataItem(

        @field:SerializedName("viewUserId")
        val viewUserId: String? = null,

        @field:SerializedName("fullName")
        val fullName: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("email")
        val email: String? = null
    )
}
