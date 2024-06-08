package com.entre.gethub.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ReysEventResponse(

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("data")
    val data: List<EventData>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("total_data")
    val totalData: Int? = null
) {
    @Parcelize
    data class EventData(
        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("image_url")
        val imageUrl: String? = null,

        @field:SerializedName("category")
        val category: String? = null,

        @field:SerializedName("is_active")
        val isActive: Int? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("similarity")
        val similarity: Double? = null
    ) : Parcelable
}
