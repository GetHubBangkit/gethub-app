package com.entre.gethub.data.repositories

import com.entre.gethub.data.remote.response.projects.ProjectsResponse
import com.entre.gethub.data.remote.retrofit.ApiService

class ProjectRepository private constructor(private val apiService: ApiService){

    suspend fun getProjects(): ProjectsResponse {
        return apiService.getProjects()
    }

    companion object {
        fun getInstance(apiService: ApiService): ProjectRepository = ProjectRepository(apiService)
    }
}