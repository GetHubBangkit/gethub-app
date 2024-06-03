package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class ThemeHubResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null
)
