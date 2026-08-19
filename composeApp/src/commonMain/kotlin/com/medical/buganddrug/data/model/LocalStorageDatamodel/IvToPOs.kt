package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class IvToPOs(
    val iVtoPOs: List<IVtoPO> = emptyList(),
    val url: String? = ""
)