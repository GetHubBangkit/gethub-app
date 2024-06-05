package com.entre.gethub.data.remote.response.profiles

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {

    data class Data(

        @field:SerializedName("sentiment_freelance_score")
        val sentimentFreelanceScore: Int? = null,

        @field:SerializedName("about")
        val about: String? = null,

        @field:SerializedName("is_complete_profile")
        val isCompleteProfile: Boolean? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("web")
        val web: String? = null,

        @field:SerializedName("role_id")
        val roleId: String? = null,

        @field:SerializedName("is_visibility")
        val isVisibility: Boolean? = null,

        @field:SerializedName("qr_code")
        val qrCode: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null,

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("sentiment_owner_analisis")
        val sentimentOwnerAnalisis: Any? = null,

        @field:SerializedName("is_verify")
        val isVerify: Boolean? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("is_verif_ktp")
        val isVerifKtp: Boolean? = null,

        @field:SerializedName("theme_hub")
        val themeHub: Int? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("is_premium")
        val isPremium: Boolean? = null,

        @field:SerializedName("sentiment_owner_score")
        val sentimentOwnerScore: Any? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("sentiment_freelance_analisis")
        val sentimentFreelanceAnalisis: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("is_verif_ktp_url")
        val isVerifKtpUrl: Any? = null
    )

}
