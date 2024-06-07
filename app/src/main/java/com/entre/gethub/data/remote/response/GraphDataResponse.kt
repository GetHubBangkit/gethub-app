package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class GraphDataResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<Data>,

    @SerializedName("message")
    val message: String,

    @SerializedName("error_code")
    val errorCode: Int
) {
    data class Data(
        @SerializedName("date")
        val date: String,

        @SerializedName("total_views")
        val totalViews: Int
    )
}
