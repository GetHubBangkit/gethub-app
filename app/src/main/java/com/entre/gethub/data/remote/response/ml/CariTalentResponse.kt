package com.entre.gethub.data.remote.response.ml

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CariTalentResponse(

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("data")
    val data: List<Data>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("total_data")
    val totalData: Int? = null
) : Parcelable {
    @Parcelize
    data class Data(

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("similarity")
        val similarity: Double? = null
    ) : Parcelable
}
