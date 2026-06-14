package com.medical.buganddrug.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PatientInfo(
    val id: Int,
    val infoId: String,
    val name: String,
    val age: Int,
    val weight: Double,
    val symptomId: Int,
    val symptomName: String,
    val reasonId: Int,
    val reasonName: String,
    val seasonId: Int?,                       // nullable
    val seasoncauseName: String?,             // nullable
    val season: String?,                      // nullable
    val hospitalStay: String,
    val heartRate: String,
    val temperature: String,
    val durationofIllness: String,
    val bloodPressureSystolic: String?,       // nullable
    val bloodPressureDiastolic: String?,      // nullable
    val signId: Int,
    val signName: String?,                    // nullable
    val respiratoryRate: String,
    val comorbidities: String,
    val indwellingdevicesprostheticimplants: String?, // nullable
    val diseasesList: String?, // nullable
    val updDateTime: String
)

