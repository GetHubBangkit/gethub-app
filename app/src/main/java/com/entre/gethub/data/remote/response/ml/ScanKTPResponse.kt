package com.entre.gethub.data.remote.response.ml

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanKTPResponse(
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("result")
    val result: Result,
    @SerializedName("message")
    val message: String
) : Parcelable {
    @Parcelize
    data class Result(
        @SerializedName("nik")
        val nik: String,
        @SerializedName("nama")
        val nama: String,
        @SerializedName("content")
        val content: String
    ) : Parcelable
}
