package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchingPartnerResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("error_code")
    val errorCode: Int
) {
    data class Data(
        @SerializedName("partners")
        val partners: List<Partner>
    )

    data class Partner(
        @SerializedName("id")
        val id: String,
        @SerializedName("full_name")
        val fullName: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("profession")
        val profession: String,
        @SerializedName("address")
         val address: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("website")
        val website: String
    )
}
