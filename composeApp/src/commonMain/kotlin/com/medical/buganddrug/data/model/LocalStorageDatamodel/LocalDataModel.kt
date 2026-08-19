package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class LocalDataModel(
    val antibioticGeneList: AntibioticGeneList? = null,
    val bacteriaSusceptibilityList: BacteriaSusceptibilityList? = null,
    val creatinineClearance: CreatinineClearance? = null,
    val cultureTherapyGuideList: CultureTherapyGuideList? = null,
    val exposureProPhylaxisList: ExposureProPhylaxisList? = null,
    val hivArtCenterList: HivArtCenterList? = null,
    val ivToPOs: IvToPOs? = null,
    val lovs: Lovs? = null,
    val precautionFinderList: PrecautionFinderList? = null,
    val syndromeIdentificationData: SyndromeIdentificationData? = null,
    val syndromeIdentificationDataQ2: SyndromeIdentificationDataQ2? = null,
    val qSofaScoringPossibilities: List<QSofaScoringPossibility>? = null,
    val news2ScoringPossibilities: List<News2ScoringPossibility>? = null
)