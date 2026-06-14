package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable

data class Symptom(
    val symptomsID: Int,
    val symptomsName: String
)