package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.data.remote.response.projects.AddProjectMilestoneResponse
import com.entre.gethub.data.remote.response.projects.AllProjectMilestoneResponse
import com.entre.gethub.data.remote.response.projects.MyProjectBidResponse
import com.entre.gethub.data.remote.response.projects.PaymentResponse
import com.entre.gethub.data.remote.response.projects.PostProjectResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectDetailResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.data.remote.response.projects.ProjectDetailResponse
import com.entre.gethub.data.remote.response.projects.ProjectResponse
import com.entre.gethub.data.remote.response.projects.ProjectStatsResponse
import com.entre.gethub.data.remote.response.projects.ReviewResponse
import com.entre.gethub.data.remote.response.projects.SearchProjectResponse
import com.entre.gethub.data.remote.response.projects.SettlementResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class ProjectRepository private constructor(private val apiService: ApiService) {

    suspend fun getProjects(): ProjectResponse {
        return apiService.getProjects()
    }

    suspend fun getProjectDetail(id: String): ProjectDetailResponse {
        return apiService.getProjectDetail(id)
    }

    suspend fun bidProject(projectId: String, budgetBid: Int, message: String): ApiResponse {
        return apiService.bidProject(projectId, budgetBid, message)
    }

    suspend fun regenerateVerifyEmail() {
        return apiService.regenerateVerifyEmail()
    }

    suspend fun getMyProjectBids(): MyProjectBidResponse {
        return apiService.getMyProjectBids()
    }

    suspend fun searchProjects(title: String): SearchProjectResponse {
        return apiService.searchProjects(title)
    }

    suspend fun postProject(
        title: String,
        categoryId: String,
        description: String,
        minBudget: Int,
        maxBudget: Int,
        minDeadline: String,
        maxDeadline: String,
    ): PostProjectResponse {
        return apiService.postProject(
            title,
            categoryId,
            description,
            minBudget,
            maxBudget,
            minDeadline,
            maxDeadline
        )
    }

    suspend fun getUserProjectStats(): ProjectStatsResponse {
        return apiService.getUserProjectStats()
    }

    suspend fun getPostedProjects(): PostedProjectResponse {
        return apiService.getPostedProjects()
    }

    suspend fun getPostedProjectDetail(id: String): PostedProjectDetailResponse {
        return apiService.getPostedProjectDetail(id)
    }

    suspend fun chooseBidder(projectId: String, freelancerId: String): ApiResponse {
        return apiService.chooseBidder(projectId, freelancerId)
    }

    suspend fun getAcceptedBids(): AcceptedProjectBidResponse {
        return apiService.getAcceptedBids()
    }

    suspend fun addMilestone(
        projectId: String,
        taskNumber: Int,
        taskDescription: String
    ): AddProjectMilestoneResponse {
        return apiService.addMilestone(projectId, taskNumber, taskDescription)
    }

    suspend fun getMilestone(projectId: String): AllProjectMilestoneResponse {
        return apiService.getMilestone(projectId)
    }

    suspend fun deleteMilestoneById(
        projectId: String,
        taskId: String,
    ): ApiResponse {
        return apiService.deleteMilestoneById(projectId, taskId)
    }

    suspend fun getSettlementOwner(projectId: String): SettlementResponse {
        return apiService.getSettlementOwner(projectId)
    }

    suspend fun generatePaymentToken(projectId: String, freelancerId: String): PaymentResponse {
        return apiService.generatePaymentToken(projectId, freelancerId)
    }

    suspend fun finishProject(projectId: String): ApiResponse {
        return apiService.finishProject(projectId)
    }

    suspend fun createReview(
        projectId: String,
        targetUserId: String,
        message: String,
        reviewType: String
    ): ReviewResponse {
        return apiService.createReview(projectId, targetUserId, message, reviewType)
    }

    companion object {
        fun getInstance(apiService: ApiService): ProjectRepository = ProjectRepository(apiService)
    }
}