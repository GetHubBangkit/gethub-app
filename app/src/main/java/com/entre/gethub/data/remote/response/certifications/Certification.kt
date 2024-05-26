package com.entre.gethub.data.remote.response.certifications

import com.google.gson.annotations.SerializedName

data class Certification(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("category_id")
    val categoryId: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)
