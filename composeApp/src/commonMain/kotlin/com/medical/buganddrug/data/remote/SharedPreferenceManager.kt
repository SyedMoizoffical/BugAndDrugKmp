package com.medical.buganddrug.data.remote


import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class SharedPreferenceManager(
    private val settings: Settings
) {

    fun savePatientData(patientData: JsonObject) {
        settings.putString("patientData", patientData.toString())
    }

    fun getPatientData(): JsonObject? {
        val json = settings.getStringOrNull("patientData")


        return json?.let { Json.parseToJsonElement(it).jsonObject }
    }

    fun saveToken(token: String) {
        settings.putString("patientToken", token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull("patientToken")
    }

    fun saveEmail(email: String) {
        settings.putString("patientEmail", email)
    }

    fun getEmail(): String? {
        return settings.getStringOrNull("patientEmail")
    }

    fun saveDisclaimer(value: String) {
        settings.putString("patientDisclaimer", value)
    }

    fun getDisclaimer(): String? {
        return settings.getStringOrNull("patientDisclaimer")
    }

    fun clearEmail() {
        settings.remove("patientEmail")
    }

    fun clearPatientData() {
        settings.remove("patientData")
    }
}
