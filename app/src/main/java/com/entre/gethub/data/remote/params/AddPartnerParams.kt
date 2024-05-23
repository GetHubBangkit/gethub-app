package com.entre.gethub.data.remote.params

data class AddPartnerParams(
    val fullname: String,
    val profession: String,
    val email: String,
    val phone: String,
    val website: String,
    val address: String,
    val photo: String?,
    val image: String?
)