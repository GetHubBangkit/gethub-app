package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class ProjectStatsResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    data class Data(

        @field:SerializedName("bid_projects")
        val bidProjects: List<BidProjectsItem?>? = null,

        @field:SerializedName("bids_made")
        val bidsMade: Int? = null,

        @field:SerializedName("job_posted")
        val jobPosted: Int? = null,

        @field:SerializedName("bids_accepted")
        val bidsAccepted: Int? = null
    )

    data class BidProjectsItem(

        @field:SerializedName("min_budget")
        val minBudget: Int? = null,

        @field:SerializedName("max_deadline")
        val maxDeadline: String? = null,

        @field:SerializedName("is_selected")
        val isSelected: Boolean? = null,

        @field:SerializedName("max_budget")
        val maxBudget: Int? = null,

        @field:SerializedName("created_date")
        val createdDate: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("min_deadline")
        val minDeadline: String? = null,

        @field:SerializedName("projectId")
        val projectId: String? = null
    )

}