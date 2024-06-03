package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class AddProjectMilestoneResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error_code")
	val errorCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("task_description")
	val taskDescription: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("task_status")
	val taskStatus: String? = null,

	@field:SerializedName("project_id")
	val projectId: String? = null,

	@field:SerializedName("task_number")
	val taskNumber: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
