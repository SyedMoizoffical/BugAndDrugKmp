package com.medical.buganddrug.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckVersionRequest(
    @SerialName("useremail") val userEmail: String,
    @SerialName("appversion") val appVersion: String,
    @SerialName("datavrsion") val dataVersion: Int = 0
)

@Serializable
data class CheckVersionResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("isAppUpdateRequired") val isAppUpdateRequired: Boolean = false,
    @SerialName("isDataUpdateRequired") val isDataUpdateRequired: Boolean = false,
    @SerialName("latestAppVersion") val latestAppVersion: String? = null,
    @SerialName("latestDataVersion") val latestDataVersion: Int? = null,
    @SerialName("releaseNotes") val releaseNotes: String? = null,
    @SerialName("message") val message: String? = null
)
