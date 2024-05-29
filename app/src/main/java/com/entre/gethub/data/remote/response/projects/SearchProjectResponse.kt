package com.entre.gethub.data.remote.response.projects

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SearchProjectResponse(

	@field:SerializedName("data")
	val data: List<ProjectsResponse.Project>,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error_code")
	val errorCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable