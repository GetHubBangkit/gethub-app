package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserPublicProfileResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null
) {
    data class Data(

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("password")
        val password: String? = null,

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("web")
        val web: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("about")
        val about: String? = null,

        @field:SerializedName("qr_code")
        val qrCode: String? = null,

        @field:SerializedName("is_verify")
        val isVerify: Boolean? = null,

        @field:SerializedName("is_complete_profile")
        val isCompleteProfile: Boolean? = null,

        @field:SerializedName("is_premium")
        val isPremium: Boolean? = null,

        @field:SerializedName("role_id")
        val roleId: String? = null,

        @field:SerializedName("theme_hub")
        val themeHub: Int? = null,

        @field:SerializedName("sentiment_owner_analisis")
        val sentimentOwnerAnalysis: Any? = null,

        @field:SerializedName("sentiment_owner_score")
        val sentimentOwnerScore: Any? = null,

        @field:SerializedName("sentiment_freelance_analisis")
        val sentimentFreelanceAnalysis: Any? = null,

        @field:SerializedName("sentiment_freelance_score")
        val sentimentFreelanceScore: Any? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("links")
        val links: List<Link>? = null,

        @field:SerializedName("products")
        val products: List<Product>? = null,

        @field:SerializedName("backgroundCard")
        val backgroundCard: BackgroundCard? = null
    ) {
        data class Link(

            @field:SerializedName("id")
            val id: String? = null,

            @field:SerializedName("user_id")
            val userId: String? = null,

            @field:SerializedName("category")
            val category: String? = null,

            @field:SerializedName("link")
            val link: String? = null,

            @field:SerializedName("createdAt")
            val createdAt: String? = null,

            @field:SerializedName("updatedAt")
            val updatedAt: String? = null
        )

        data class Product(

            @field:SerializedName("id")
            val id: String? = null,

            @field:SerializedName("user_id")
            val userId: String? = null,

            @field:SerializedName("name")
            val name: String? = null,

            @field:SerializedName("description")
            val description: String? = null,

            @field:SerializedName("category_id")
            val categoryId: String? = null,

            @field:SerializedName("image_url")
            val imageUrl: String? = null,

            @field:SerializedName("createdAt")
            val createdAt: String? = null,

            @field:SerializedName("updatedAt")
            val updatedAt: String? = null
        )

        data class BackgroundCard(

            @field:SerializedName("bg")
            val bg: String? = null,

            @field:SerializedName("icon")
            val icon: String? = null,

            @field:SerializedName("card")
            val card: String? = null
        )
    }
}
