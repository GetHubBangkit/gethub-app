package com.entre.gethub.data.remote.retrofit

import com.entre.gethub.data.remote.response.ApiResponse
import com.entre.gethub.data.remote.response.CategoriesResponse
import com.entre.gethub.data.remote.response.InformationHubResponse
import com.entre.gethub.data.remote.response.LinkResponse
import com.entre.gethub.data.remote.response.SponsorResponse
import com.entre.gethub.data.remote.response.UploadFileResponse
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.data.remote.response.partners.AddPartnerResponse
import com.entre.gethub.data.remote.response.partners.GetHubPartnerListResponse
import com.entre.gethub.data.remote.response.products.ProductListResponse
import com.entre.gethub.data.remote.response.products.ProductResponse
import com.entre.gethub.data.remote.response.profiles.UpdateUserProfileResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // Auth
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("full_name") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse
    // Auth

    // Complete Profile
    @GET("profile")
    suspend fun getUserProfile(): UserProfileResponse

    @FormUrlEncoded
    @PUT("profile")
    suspend fun updateUserProfile(
        @Field("full_name") fullname: String,
        @Field("profession") profession: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("web") web: String,
        @Field("address") address: String,
        @Field("photo") photo: String?,
    ): UpdateUserProfileResponse
    // Complete Profile

    // Upload File
    @Multipart
    @Headers("Accept: application/json")
    @POST("upload-file")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): UploadFileResponse
    // Upload File

    // InformationHub
    @GET("informations")
    suspend fun getInformationHub(): InformationHubResponse

    // Add Partner
    @FormUrlEncoded
    @POST("partner")
    suspend fun addPartner(
        @Field("full_name") fullname: String,
        @Field("profession") profession: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("website") website: String,
        @Field("address") address: String,
        @Field("photo") photo: String?,
        @Field("image") image: String?,
    ): AddPartnerResponse
    // Add Partner

    // Get Partner List
    @GET("partners")
    suspend fun getPartnerList(): GetHubPartnerListResponse

    // Sponsors
    @GET("sponsors")
    suspend fun getSponsors(): SponsorResponse

    // Products
    @GET("products")
    suspend fun getProductList(): ProductListResponse

    @FormUrlEncoded
    @POST("product")
    suspend fun addProduct(
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("image_url") imageUrl: String,
        @Field("category_id") category: String,
    ): ProductResponse

    @GET("product/{id}")
    suspend fun getProductDetail(
        @Path("id") id: String
    ): ProductResponse

    @FormUrlEncoded
    @PUT("product/{id}")
    suspend fun editProduct(
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("image_url") imageUrl: String
    ): ProductResponse

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String,
    ): ApiResponse
    // Products

    // Links
    @FormUrlEncoded
    @POST("link")
    suspend fun addLink(
        @Field("category") category: String,
        @Field("link") link: String
    ): LinkResponse

    @GET("links")
    suspend fun getLinks(): LinkResponse

    @DELETE("link/{linkId}")
    suspend fun deleteLink(
        @Path("linkId") linkId: String
    ): LinkResponse
    // Links

    // Categories
    @GET("categories")
    suspend fun getCategories(): CategoriesResponse
    // Categories
}