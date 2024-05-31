package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.data.remote.response.projects.MyProjectBidResponse
import com.entre.gethub.data.remote.response.projects.PostProjectResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectDetailResponse
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.data.remote.response.projects.ProjectDetailResponse
import com.entre.gethub.data.remote.response.projects.ProjectStatsResponse
import com.entre.gethub.data.remote.response.projects.ProjectsResponse
import com.entre.gethub.data.remote.response.projects.SearchProjectResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class ProjectRepository private constructor(private val apiService: ApiService) {

    suspend fun getProjects(): ProjectsResponse {
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

    companion object {
        fun getInstance(apiService: ApiService): ProjectRepository = ProjectRepository(apiService)
    }
}