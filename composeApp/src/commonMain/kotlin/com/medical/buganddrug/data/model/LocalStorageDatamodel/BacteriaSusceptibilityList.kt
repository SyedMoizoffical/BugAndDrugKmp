package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class BacteriaSusceptibilityList(
    val gridLists: List<GridLists> = emptyList()
)