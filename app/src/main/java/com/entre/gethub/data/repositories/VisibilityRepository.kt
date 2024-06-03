    package com.entre.gethub.data.repositories

    import com.entre.gethub.data.remote.retrofit.ApiService
    import com.entre.gethub.data.remote.response.VisibilityResponse

    class VisibilityRepository(private val apiService: ApiService) {



        // Update the visibility status
        suspend fun setVisibility(isVisible: Boolean): VisibilityResponse {
            return apiService.updatePostVisibility(isVisible)
        }


        companion object {
            fun getInstance(apiService: ApiService): VisibilityRepository {
                return VisibilityRepository(apiService)
            }
        }
    }
