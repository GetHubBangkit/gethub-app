package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class ProjectResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {

    data class UsersBidItem(

        @field:SerializedName("id")
        val id: String? = null
    )

    data class Data(

        @field:SerializedName("projects")
        val projects: List<ProjectsItem>,

        @field:SerializedName("total_projects")
        val totalProjects: Int? = null
    )

    data class OwnerProject(

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("is_premium")
        val isPremium: Boolean,

        @field:SerializedName("is_verif_ktp")
        val isVerifKtp: Boolean,
    )

    data class Category(

        @field:SerializedName("name")
        val name: String? = null
    )

    data class ProjectsItem(

        @field:SerializedName("fee_freelance_transaction_value")
        val feeFreelanceTransactionValue: Any? = null,

        @field:SerializedName("owner_id")
        val ownerId: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("fee_owner_transaction_persen")
        val feeOwnerTransactionPersen: Any? = null,

        @field:SerializedName("max_budget")
        val maxBudget: Int? = null,

        @field:SerializedName("users_bid")
        val usersBid: List<Any?>? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("min_deadline")
        val minDeadline: String? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("status_payment")
        val statusPayment: String? = null,

        @field:SerializedName("fee_owner_transaction_value")
        val feeOwnerTransactionValue: Any? = null,

        @field:SerializedName("status_project")
        val statusProject: String? = null,

        @field:SerializedName("category_id")
        val categoryId: String? = null,

        @field:SerializedName("fee_freelance_transaction_persen")
        val feeFreelanceTransactionPersen: Any? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("chatroom_id")
        val chatroomId: String? = null,

        @field:SerializedName("total_bids")
        val totalBids: Int? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("deadline_duration")
        val deadlineDuration: String? = null,

        @field:SerializedName("is_active")
        val isActive: Boolean? = null,

        @field:SerializedName("min_budget")
        val minBudget: Int? = null,

        @field:SerializedName("banned_message")
        val bannedMessage: Any? = null,

        @field:SerializedName("max_deadline")
        val maxDeadline: String? = null,

        @field:SerializedName("status_freelance_task")
        val statusFreelanceTask: String? = null,

        @field:SerializedName("owner_project")
        val ownerProject: OwnerProject? = null,

        @field:SerializedName("created_date")
        val createdDate: String? = null,

        @field:SerializedName("category")
        val category: Category? = null,

        @field:SerializedName("project_tasks")
        val projectTasks: List<Any?>? = null
    )

}
