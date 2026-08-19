package com.medical.buganddrug.data.model.LocalStorageDatamodel
import com.medical.buganddrug.data.model.QoestionsModel.Q4Model.Isolation
import kotlinx.serialization.Serializable

@Serializable
data class PrecautionFinderList(
    val isolations: List<Isolation> = emptyList()
)