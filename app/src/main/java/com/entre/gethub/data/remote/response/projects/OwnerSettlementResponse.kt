package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class OwnerSettlementResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null
) {
    data class Data(

        @field:SerializedName("total")
        val total: Int? = null,

        @field:SerializedName("service_fee")
        val serviceFee: Int? = null,

        @field:SerializedName("deposit")
        val deposit: Int? = null
    )

}
