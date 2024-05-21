package com.bangkit.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)