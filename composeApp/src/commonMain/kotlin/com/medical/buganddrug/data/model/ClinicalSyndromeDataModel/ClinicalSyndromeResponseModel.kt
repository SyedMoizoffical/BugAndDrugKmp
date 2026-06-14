package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable
data class
ClinicalSyndromeResponseModel(
    val disease: List<ClinicalSyndromeDisease>,
    val clinicalSyndromeSymptom: List<ClinicalSyndromeSymptom>? = null,
    val syndromes: List<ClinicalSyndromeSyndrome>,
    val ClinicalSyndromesyndromeTests: List<ClinicalSyndromesyndromeTests>? = null,
    val diseaseIdenticifationlists: List<ClinicalSyndromeDiseaseIdentificationLists>,
    val etilogicalAgents: List<ClinicalSyndromeEtilogicalAgentsLists>,
    val clinicalSyndromeDiseaseSignlist: List<ClinicalSyndromeDiseaseSignlist>? = null,

)