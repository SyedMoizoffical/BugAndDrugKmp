package com.medical.buganddrug.ui.onboarding.loginScreen

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("Name") val Name: String,
    @SerialName("Email") val Email: String,
    @SerialName("Password") val Password: String,
    @SerialName("PMDC") val PMDC: String
)

@Serializable
data class LoginRequest(
    @SerialName("Email") val Email: String,
    @SerialName("Password") val Password: String
)

@Serializable
data class SignUpResponseData(
    val userId: String? = null,
    val username: String? = null,
    val requiresEmailVerification: Boolean? = null,
    val devOtp: String? = null
)

@Serializable
data class VerifyEmailRequest(
    @SerialName("Email") val Email: String,
    @SerialName("Otp") val Otp: String
)

@Serializable
data class VerifyEmailResponseData(
    val token: String? = null,
    val expiry: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val refreshToken: String? = null
)

@Serializable
data class ResendOtpRequest(
    @SerialName("Email") val Email: String
)

@Serializable
data class ResendOtpResponseData(
    val devOtp: String? = null
)

@Serializable
data class SignUpResponseDataModel(
    val expiry: String? = null,
    val token: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val requiresEmailVerification: Boolean? = null,
    val devOtp: String? = null,
    val refreshToken: String? = null
)

@Serializable
data class DeleteAccountRequest(
    @SerialName("Email") val Email: String
)