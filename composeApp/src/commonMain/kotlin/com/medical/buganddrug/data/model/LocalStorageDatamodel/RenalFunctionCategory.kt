package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class RenalFunctionCategory(
    val eGFRFrom: String? = "",
    val eGFRTo: String? = "",
    val id: Int? = 0,
    val renalFunctionCategory: String? = ""
)