package com.entre.gethub.data.remote.response.ml

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)