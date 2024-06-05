    package com.entre.gethub.data.repositories

    import com.entre.gethub.data.remote.response.ApiResponse
    import com.entre.gethub.data.remote.retrofit.ApiService
    import com.entre.gethub.data.remote.response.VisibilityResponse
    import com.google.gson.Gson
    import retrofit2.HttpException
    import com.entre.gethub.data.Result
    import com.entre.gethub.data.remote.response.LinkResponse

    class VisibilityRepository(private val apiService: ApiService) {
        suspend fun setVisibility(isVisible: Boolean): VisibilityResponse {
            return apiService.updatePostVisibility(isVisible)
        }

        companion object {
            fun getInstance(apiService: ApiService): VisibilityRepository {
                return VisibilityRepository(apiService)
            }
        }
    }


