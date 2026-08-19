package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class AntibioticDose(
    val adjustedDosing: String? = "",
    val antibioticClass: String? = "",
    val antibioticId: Int? = 0,
    val antibioticName: String? = "",
    val creatinineClearanceRange: String? = "",
    val drugInteractions: String? = "",
    val id: Int? = 0,
    val indications: String? = "",
    val lactationClass: String? = "",
    val preferredAgainst: String? = "",
    val pregnancyClass: String? = "",
    val standardDose: String? = "",
    val whoawareCategory: String? = "",
    val whoawareCategoryColor: String? = ""
)