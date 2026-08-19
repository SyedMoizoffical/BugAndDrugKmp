package com.medical.buganddrug.util

import com.medical.buganddrug.data.model.LocalStorageDatamodel.AntibioticDose
import com.medical.buganddrug.data.model.LocalStorageDatamodel.CreatinineClearance
import com.medical.buganddrug.data.model.LocalStorageDatamodel.Disease
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseIdenticifationlists
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseIdenticifationlistsX
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseItem
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseX
import com.medical.buganddrug.data.model.LocalStorageDatamodel.EtilogicalAgent
import com.medical.buganddrug.data.model.LocalStorageDatamodel.Lovs
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SyndromeIdentificationData
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SyndromeIdentificationDataQ2

/**
 * Utility object for extracting disease, organism, and antibiotic lists directly from local database models.
 * Maintains a thread-safe cache for instant access across all app screens.
 */
object DiseaseExtractor {

    var cachedDiseaseList: List<DiseaseItem> = emptyList()

    /**
     * Extract a list of DiseaseItem (type = "disease") from Lovs local database entity.
     */
    fun extractFromLovs(lovs: Lovs?): List<DiseaseItem> {
        val diseases = lovs?.diseases ?: return emptyList()
        return diseases.mapNotNull { disease ->
            val name = disease?.diseaseName
            if (!name.isNullOrBlank()) {
                DiseaseItem(
                    name = name.trim(),
                    id = disease.diseaseID ?: disease.id ?: 0,
                    type = "disease"
                )
            } else null
        }
    }

    /**
     * Extract organisms (type = "organism") from EtilogicalAgent list in local database.
     */
    fun extractOrganisms(agents: List<EtilogicalAgent>?): List<DiseaseItem> {
        if (agents == null) return emptyList()
        return agents.mapNotNull { agent ->
            val name = agent.organism
            if (!name.isNullOrBlank()) {
                DiseaseItem(
                    name = name.trim(),
                    id = agent.id ?: 0,
                    type = "organism"
                )
            } else null
        }
    }

    /**
     * Extract antibiotics (type = "antibiotic") from AntibioticDose list in local database.
     */
    fun extractAntibiotics(doses: List<AntibioticDose>?): List<DiseaseItem> {
        if (doses == null) return emptyList()
        return doses.mapNotNull { dose ->
            val name = dose.antibioticName
            if (!name.isNullOrBlank()) {
                DiseaseItem(
                    name = name.trim(),
                    id = dose.antibioticId ?: dose.id ?: 0,
                    type = "antibiotic"
                )
            } else null
        }
    }

    /**
     * Extract diseases and organisms from SyndromeIdentificationData local model.
     */
    fun extractFromSyndromeData(data: SyndromeIdentificationData?): List<DiseaseItem> {
        val list = mutableListOf<DiseaseItem>()
        // Diseases
        data?.disease?.forEach { diseaseX ->
            val name = diseaseX.diseaseName
            if (!name.isNullOrBlank()) {
                list.add(
                    DiseaseItem(
                        name = name.trim(),
                        id = diseaseX.diseaseID ?: 0,
                        type = "disease"
                    )
                )
            }
        }
        data?.diseaseIdenticifationlists?.forEach { item ->
            val name = item.disease
            if (!name.isNullOrBlank()) {
                list.add(
                    DiseaseItem(
                        name = name.trim(),
                        id = item.diseaseId ?: item.id ?: 0,
                        type = "disease"
                    )
                )
            }
        }
        // Organisms from etilogicalAgents
        list.addAll(extractOrganisms(data?.etilogicalAgents))
        return list
    }

    /**
     * Extract diseases and organisms from SyndromeIdentificationDataQ2 local model.
     */
    fun extractFromSyndromeDataQ2(data: SyndromeIdentificationDataQ2?): List<DiseaseItem> {
        val list = mutableListOf<DiseaseItem>()
        // Diseases
        data?.disease?.forEach { diseaseX ->
            val name = diseaseX.diseaseName
            if (!name.isNullOrBlank()) {
                list.add(
                    DiseaseItem(
                        name = name.trim(),
                        id = diseaseX.diseaseID ?: 0,
                        type = "disease"
                    )
                )
            }
        }
        data?.diseaseIdenticifationlists?.forEach { item ->
            val name = item.disease
            if (!name.isNullOrBlank()) {
                list.add(
                    DiseaseItem(
                        name = name.trim(),
                        id = item.diseaseId ?: item.id ?: 0,
                        type = "disease"
                    )
                )
            }
        }
        // Organisms from etilogicalAgents
        list.addAll(extractOrganisms(data?.etilogicalAgents))
        return list
    }

    /**
     * Extract combined list of diseases, organisms, and antibiotics strictly from local database entities.
     * Updates [cachedDiseaseList] for app-wide use.
     */
    fun getCombinedDiseaseList(
        lovs: Lovs? = null,
        syndromeData: SyndromeIdentificationData? = null,
        syndromeDataQ2: SyndromeIdentificationDataQ2? = null,
        creatinineClearance: CreatinineClearance? = null
    ): List<DiseaseItem> {
        val combined = mutableListOf<DiseaseItem>()
        combined.addAll(extractFromLovs(lovs))
        combined.addAll(extractFromSyndromeData(syndromeData))
        combined.addAll(extractFromSyndromeDataQ2(syndromeDataQ2))
        combined.addAll(extractAntibiotics(creatinineClearance?.antibioticDoses))

        val result = combined.distinctBy { it.name.lowercase() }
        if (result.isNotEmpty()) {
            cachedDiseaseList = result
        }
        return result
    }
}
