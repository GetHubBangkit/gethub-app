package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class PostProjectResponse(

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

	@field:SerializedName("deadline_duration")
	val deadlineDuration: String? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("owner_id")
	val ownerId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("max_budget")
	val maxBudget: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("min_deadline")
	val minDeadline: String? = null,

	@field:SerializedName("min_budget")
	val minBudget: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("max_deadline")
	val maxDeadline: String? = null,

	@field:SerializedName("status_freelance_task")
	val statusFreelanceTask: String? = null,

	@field:SerializedName("status_payment")
	val statusPayment: String? = null,

	@field:SerializedName("status_project")
	val statusProject: String? = null,

	@field:SerializedName("category_id")
	val categoryId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("chatroom_id")
	val chatroomId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
