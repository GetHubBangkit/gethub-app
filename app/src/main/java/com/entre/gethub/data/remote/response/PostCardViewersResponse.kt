package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostCardViewersResponse(

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error_code")
    val errorCode: Int
) {
    data class Data(
        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("profile_user_id")
        val profileUserId: String,

        @field:SerializedName("view_user_id")
        val viewUserId: String,

        @field:SerializedName("date")
        val date: String,

        @field:SerializedName("createdAt")
        val createdAt: String,

        @field:SerializedName("updatedAt")
        val updatedAt: String
    )
}
