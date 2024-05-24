package com.entre.gethub.data.remote.response


import com.google.gson.annotations.SerializedName

data class LinkResponse(

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

        @field:SerializedName("category")
        val category: String? = null,

        @field:SerializedName("link")
        val link: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}
