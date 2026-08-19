package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class SyndromeIdentificationDataQ2(
    val disease: List<DiseaseX> = emptyList(),
    val diseaseIdenticifationlists: List<DiseaseIdenticifationlistsX> = emptyList(),
    val diseaseSignlist: List<DiseaseSignlist> = emptyList(),
    val etilogicalAgents: List<EtilogicalAgent> = emptyList(),
    val symptom: List<SymptomXX> = emptyList(),
    val syndromeTests: List<SyndromeTest> = emptyList(),
    val syndromes: List<SyndromeX> = emptyList()
)