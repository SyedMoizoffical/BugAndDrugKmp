package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class Sign(
    val signId: Int? = 0,
    val signName: String? = ""
)