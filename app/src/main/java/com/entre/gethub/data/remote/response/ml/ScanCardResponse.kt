package com.entre.gethub.data.remote.response.ml

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanCardResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable {
    @Parcelize
    data class Data(

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("web")
        val web: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("email")
        val email: String? = null
    ) : Parcelable

}