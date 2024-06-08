package com.entre.gethub.data.remote.response.premium

import com.google.gson.annotations.SerializedName

data class PaymentHistoryResponse(

    @field:SerializedName("data")
    val data: List<DataItem>,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    data class DataItem(

        @field:SerializedName("transaction_date")
        val transactionDate: String? = null,

        @field:SerializedName("amount")
        val amount: Int? = null,

        @field:SerializedName("status")
        val status: String? = null
    )
}
