package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class ReviewResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    data class Data(

        @field:SerializedName("freelancer_id")
        val freelancerId: String? = null,

        @field:SerializedName("sentiment")
        val sentiment: String? = null,

        @field:SerializedName("sentiment_score")
        val sentimentScore: Any? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("project_id")
        val projectId: String? = null,

        @field:SerializedName("owner_id")
        val ownerId: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    )

}
