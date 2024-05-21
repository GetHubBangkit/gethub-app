package com.bangkit.gethub.data.remote.params

data class UpdateUserProfileParams(
    val fullname: String,
    val profession: String,
    val email: String,
    val phone: String,
    val web: String,
    val address: String,
    val photo: String?
)
