package com.entre.gethub.data.remote.response.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("data")
    val user: UserData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable {
    @Parcelize
    data class UserData(

        @field:SerializedName("profession")
        val profession: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("is_verify")
        val isVerify: Boolean? = null,

        @field:SerializedName("about")
        val about: String? = null,

        @field:SerializedName("photo")
        val photo: String? = null,

        @field:SerializedName("is_complete_profile")
        val isCompleteProfile: Boolean? = null,

        @field:SerializedName("token")
        val token: String? = null,

        @field:SerializedName("theme_hub")
        val themeHub: Int? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("full_name")
        val fullName: String? = null,

        @field:SerializedName("is_premium")
        val isPremium: Boolean? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("web")
        val web: String? = null,

        @field:SerializedName("role_id")
        val roleId: String? = null,

        @field:SerializedName("qr_code")
        val qrCode: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("username")
        val username: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    ) : Parcelable
}
