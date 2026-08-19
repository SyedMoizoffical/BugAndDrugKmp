package com.medical.buganddrug.data.model.LocalStorageDatamodel

import kotlinx.serialization.Serializable

@Serializable

data class Lovs(
    val diseases: List<Disease>,
    val immunoReson: List<ImmunoReson>,
    val indwellingDevices: List<IndwellingDevice>,
    val seasonalities: List<Seasonality>,
    val sign: List<Sign>,
    val symptoms: List<SymptomX>
)