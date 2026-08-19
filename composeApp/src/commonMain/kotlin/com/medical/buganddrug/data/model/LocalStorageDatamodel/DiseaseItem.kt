package com.medical.buganddrug.data.model.LocalStorageDatamodel

import kotlinx.serialization.Serializable

/**
 * Data class representing a disease item extracted from local database or API.
 * Stores 3 key variables:
 * - name: Name of the disease (e.g. "Malaria")
 * - id: Unique identifier (e.g. 1)
 * - type: Disease category or type (e.g. "disease" or coreType)
 */
@Serializable
data class DiseaseItem(
    val name: String = "",
    val id: Int = 0,
    val type: String = "disease"
)
