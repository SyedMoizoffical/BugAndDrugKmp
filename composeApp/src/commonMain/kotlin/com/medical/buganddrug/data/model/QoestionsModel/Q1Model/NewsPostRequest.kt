package com.medical.buganddrug.data.model.QoestionsModel.Q1Model

import kotlinx.serialization.Serializable

@Serializable
data class NewsPostRequest(
    val infoId: String,
    val Pulse: NewsItem,
    val RoomAirOrSupplementalO2: NewsItem,
    val RespiratoryRate: NewsItem,
    val SystolicBP: NewsItem,
    val HypercapnicRespiratoryFailure: NewsItem,
    val Temperature: NewsItem,
    val Spo2: NewsItem,
    val Consciousness: NewsItem
)
@Serializable

data class NewsItem(
    val Name: String,
    val Value: String,
    val Score: Int
)
