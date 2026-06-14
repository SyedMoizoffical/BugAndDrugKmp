package com.medical.buganddrug.ui.onboarding.loginScreen

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponseDataModel(
    val expiry: String,
    val token: String,
    val userId: String,
    val username: String
)