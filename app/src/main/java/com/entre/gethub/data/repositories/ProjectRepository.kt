package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.projects.MyProjectBidResponse
import com.entre.gethub.data.remote.response.projects.ProjectDetailResponse
import com.entre.gethub.data.remote.response.projects.ProjectsResponse
import com.entre.gethub.data.remote.response.projects.SearchProjectResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class ProjectRepository private constructor(private val apiService: ApiService){

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

    companion object {
        fun getInstance(apiService: ApiService): ProjectRepository = ProjectRepository(apiService)
    }
}