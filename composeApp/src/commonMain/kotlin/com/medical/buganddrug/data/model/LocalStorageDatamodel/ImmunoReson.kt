package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class ImmunoReson(
    val pathogens: String? = "",
    val reasonId: Int? = 0,
    val reasonName: String? = ""
)