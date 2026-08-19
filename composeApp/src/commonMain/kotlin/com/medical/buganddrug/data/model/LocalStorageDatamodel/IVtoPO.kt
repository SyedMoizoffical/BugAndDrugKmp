package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class IVtoPO(
    val diseaseId: Int? = 0,
    val diseaseName: String? = "",
    val iV: String? = "",
    val id: Int? = 0,
    val poEquivalent: String? = "",
    val switchNote: String? = ""
)