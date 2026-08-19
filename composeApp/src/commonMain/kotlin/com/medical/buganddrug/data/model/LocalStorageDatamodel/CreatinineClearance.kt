package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class CreatinineClearance(
    val antibioticDoses: List<AntibioticDose> = emptyList(),
    val renalFunctionCategories: List<RenalFunctionCategory> = emptyList()
)