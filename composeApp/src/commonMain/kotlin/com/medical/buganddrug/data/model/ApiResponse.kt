package com.medical.buganddrug.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val `data`: T? = null,
    val msg: String? = null,
    val statusCode: Int,
    val statusMessage: String? = null,
    val success: Boolean
)
