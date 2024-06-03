package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnaliticTotalResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null
) {
    data class Data(

        @field:SerializedName("total_card_viewers")
        val totalCardViewers: Int? = null,

        @field:SerializedName("total_web_viewers")
        val totalWebViewers: Int? = null,

        @field:SerializedName("total_partner")
        val totalPartner: Int? = null
    )
}
