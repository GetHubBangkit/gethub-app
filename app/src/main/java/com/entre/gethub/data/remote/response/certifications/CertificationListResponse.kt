package com.entre.gethub.data.remote.response.certifications

import com.google.gson.annotations.SerializedName

data class CertificationListResponse(

    @field:SerializedName("data")
	val data: List<Certification>,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("error_code")
	val errorCode: Int? = null,

    @field:SerializedName("message")
	val message: String? = null
)

