package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class DiseaseSignlist(
    val diseaseId: Int? = 0,
    val id: Int? = 0,
    val signId: Int? = 0,
    val signName: String? = ""
)