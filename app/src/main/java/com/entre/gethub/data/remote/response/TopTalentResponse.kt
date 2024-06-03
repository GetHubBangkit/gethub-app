package com.entre.gethub.data.remote.response

import com.google.gson.annotations.SerializedName

data class TopTalentResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<TopTalent>,

    @SerializedName("message")
    val message: String,

    @SerializedName("error_code")
    val errorCode: Int
)

data class TopTalent(
    @SerializedName("id")
    val id: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("profession")
    val profession: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("web")
    val web: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("about")
    val about: String? = null,

    @SerializedName("qr_code")
    val qrCode: String,

    @SerializedName("is_verify")
    val isVerify: Boolean,

    @SerializedName("is_complete_profile")
    val isCompleteProfile: Boolean,

    @SerializedName("is_premium")
    val isPremium: Boolean,

    @SerializedName("is_verif_ktp")
    val isVerifKtp: Boolean,

    @SerializedName("is_verif_ktp_url")
    val isVerifKtpUrl: String? = null,

    @SerializedName("role_id")
    val roleId: String? = null,

    @SerializedName("theme_hub")
    val themeHub: Int,

    @SerializedName("sentiment_owner_analisis")
    val sentimentOwnerAnalisis: String? = null,

    @SerializedName("sentiment_owner_score")
    val sentimentOwnerScore: Int? = null,

    @SerializedName("sentiment_freelance_analisis")
    val sentimentFreelanceAnalisis: String,

    @SerializedName("sentiment_freelance_score")
    val sentimentFreelanceScore: Int,

    @SerializedName("is_visibility")
    val isVisibility: Boolean,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)
