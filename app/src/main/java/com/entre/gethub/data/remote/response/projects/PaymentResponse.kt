package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    data class Data(

        @field:SerializedName("redirect_url")
        val redirectUrl: String,

        @field:SerializedName("token")
        val token: String? = null
    )
}