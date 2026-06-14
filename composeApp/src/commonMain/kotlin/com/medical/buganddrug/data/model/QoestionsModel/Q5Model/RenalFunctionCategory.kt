package com.medical.buganddrug.data.model.QoestionsModel.Q5Model
import kotlinx.serialization.Serializable

@Serializable
data class RenalFunctionCategory(
    val id: Int,
    val eGFRFrom: String,
    val eGFRTo: String,
    val renalFunctionCategory: String
)

@Serializable
data class AntibioticDose(
    val id: Int,
    val antibioticClass: String,
    val antibioticId: Int,
    val antibioticName: String,
    val standardDose: String,
    val creatinineClearanceRange: String,
    val adjustedDosing: String,

    val whoawareCategory: String?,
    val whoawareCategoryColor: String?,
    val indications: String,
    val preferredAgainst: String,
    val lactationClass: String,
    val pregnancyClass: String,
    val drugInteractions: String,
)

@Serializable
data class Q5Response(
    val renalFunctionCategories: List<RenalFunctionCategory>,
    val antibioticDoses: List<AntibioticDose>
)
