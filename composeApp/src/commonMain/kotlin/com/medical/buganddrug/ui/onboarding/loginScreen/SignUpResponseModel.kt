package com.medical.buganddrug.ui.onboarding.loginScreen
import kotlinx.serialization.Serializable


data class SignUpResponseModel(
    val `data`: SignUpResponseDataModel?,
    val msg: String,
    val statusCode: Int,
    val statusMessage: Any,
    val success: Boolean
)