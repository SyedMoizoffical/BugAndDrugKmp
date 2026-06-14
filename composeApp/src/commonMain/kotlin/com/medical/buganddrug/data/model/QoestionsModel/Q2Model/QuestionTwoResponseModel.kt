package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable

data class
QuestionTwoResponseModel(
    val disease: List<Disease>,
    val symptom: List<Symptom>,
    val syndromes: List<Syndrome>,
    val syndromeTests: List<syndromeTests>,
    val diseaseIdenticifationlists: List<DiseaseIdentificationLists>,
    val etilogicalAgents: List<EtilogicalAgentsLists>,
    val diseaseSignlist: List<DiseaseSignlist>
)