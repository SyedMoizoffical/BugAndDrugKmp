package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val immunoReson: List<ImmunoReson>,
    val indwellingDevices: List<IndwellingDevice>,
    val seasonalities: List<Seasonality>,
    val sign: List<Sign>,
    val symptoms: List<Symptom>,
    val diseases: List<Disease>

)