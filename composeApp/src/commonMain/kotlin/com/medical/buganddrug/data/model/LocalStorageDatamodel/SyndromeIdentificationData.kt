package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class SyndromeIdentificationData(
    val disease: List<DiseaseX> = emptyList(),
    val diseaseIdenticifationlists: List<DiseaseIdenticifationlists> = emptyList(),
    val diseaseSignlist: List<DiseaseSignlist> = emptyList(),
    val etilogicalAgents: List<EtilogicalAgent> = emptyList(),
    val symptom: List<SymptomXX> = emptyList(),
    val syndromeTests: List<SyndromeTest> = emptyList(),
    val syndromes: List<SyndromeX> = emptyList()
)