package com.medical.buganddrug.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.medical.buganddrug.data.model.CultureTherapyGuideModel.CultureTherapyGuideDto
import com.medical.buganddrug.data.model.LocalStorageDatamodel.AntibioticDose
import com.medical.buganddrug.data.model.LocalStorageDatamodel.Disease
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseIdenticifationlists
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseIdenticifationlistsX
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseSignlist
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseX
import com.medical.buganddrug.data.model.LocalStorageDatamodel.EtilogicalAgent
import com.medical.buganddrug.data.model.LocalStorageDatamodel.ExposureProPhylaxis
import com.medical.buganddrug.data.model.LocalStorageDatamodel.GridLists
import com.medical.buganddrug.data.model.LocalStorageDatamodel.HivArtCenter
import com.medical.buganddrug.data.model.LocalStorageDatamodel.News2ScoringPossibility
import com.medical.buganddrug.data.model.LocalStorageDatamodel.QSofaScoringPossibility
import com.medical.buganddrug.data.model.LocalStorageDatamodel.IVtoPO
import com.medical.buganddrug.data.model.LocalStorageDatamodel.ImmunoReson
import com.medical.buganddrug.data.model.LocalStorageDatamodel.IndwellingDevice
import com.medical.buganddrug.data.model.LocalStorageDatamodel.RenalFunctionCategory
import com.medical.buganddrug.data.model.LocalStorageDatamodel.Seasonality
import com.medical.buganddrug.data.model.LocalStorageDatamodel.Sign
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SymptomX
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SymptomXX
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SyndromeTest
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SyndromeX
import com.medical.buganddrug.data.model.QoestionsModel.Q4Model.Isolation
import com.medical.buganddrug.data.model.QoestionsModel.Q8Model.AntibioticGeneListDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
@Entity(tableName = "lovs")
data class LovsEntity(
    @PrimaryKey val id: Int = 1,
    val diseases: List<Disease>,
    val immunoReson: List<ImmunoReson>,
    val indwellingDevices: List<IndwellingDevice>,
    val seasonalities: List<Seasonality>,
    val sign: List<Sign>,
    val symptoms: List<SymptomX>
)

@Entity(tableName = "syndrome_identification_data_q2")
data class SyndromeIdentificationDataQ2Entity(
    @PrimaryKey val id: Int = 1,
    val disease: List<DiseaseX>,
    val diseaseIdenticifationlists: List<DiseaseIdenticifationlistsX>,
    val diseaseSignlist: List<DiseaseSignlist>,
    val etilogicalAgents: List<EtilogicalAgent>,
    val symptom: List<SymptomXX>,
    val syndromeTests: List<SyndromeTest>,
    val syndromes: List<SyndromeX>
)

@Entity(tableName = "syndrome_identification_data")
data class SyndromeIdentificationDataEntity(
    @PrimaryKey val id: Int = 1,
    val disease: List<DiseaseX>,
    val diseaseIdenticifationlists: List<DiseaseIdenticifationlists>,
    val diseaseSignlist: List<DiseaseSignlist>,
    val etilogicalAgents: List<EtilogicalAgent>,
    val symptom: List<SymptomXX>,
    val syndromeTests: List<SyndromeTest>,
    val syndromes: List<SyndromeX>
)

@Entity(tableName = "iv_to_pos")
data class IvToPOsEntity(
    @PrimaryKey val id: Int = 1,
    val iVtoPOs: List<IVtoPO>,
    val url: String
)

@Entity(tableName = "precaution_finder_list")
data class PrecautionFinderListEntity(
    @PrimaryKey val id: Int = 1,
    val isolations: List<Isolation>
)

@Entity(tableName = "creatinine_clearance")
data class CreatinineClearanceEntity(
    @PrimaryKey val id: Int = 1,
    val antibioticDoses: List<AntibioticDose>,
    val renalFunctionCategories: List<RenalFunctionCategory>
)

@Entity(tableName = "antibiotic_gene_list")
data class AntibioticGeneListEntity(
    @PrimaryKey val id: Int = 1,
    val antibioticGeneListDtos: List<AntibioticGeneListDto>
)

@Entity(tableName = "exposure_prophylaxis_list")
data class ExposureProPhylaxisListEntity(
    @PrimaryKey val id: Int = 1,
    val exposureProPhylaxisList: List<ExposureProPhylaxis>
)

@Entity(tableName = "bacteria_susceptibility_list")
data class BacteriaSusceptibilityListEntity(
    @PrimaryKey val id: Int = 1,
    val gridLists: List<GridLists>
)

@Entity(tableName = "hiv_art_center_list")
data class HivArtCenterListEntity(
    @PrimaryKey val id: Int = 1,
    val hivArtCenters: List<HivArtCenter>
)

@Entity(tableName = "culture_therapy_guide_list")
data class CultureTherapyGuideListEntity(
    @PrimaryKey val id: Int = 1,
    val cultureTherapyGuidelistDtos: List<CultureTherapyGuideDto>
)

@Entity(tableName = "qsofa_scoring_possibilities")
data class QSofaScoringPossibilitiesEntity(
    @PrimaryKey val id: Int = 1,
    val qSofaScoringPossibilities: List<QSofaScoringPossibility>
)

@Entity(tableName = "news2_scoring_possibilities")
data class News2ScoringPossibilitiesEntity(
    @PrimaryKey val id: Int = 1,
    val news2ScoringPossibilities: List<News2ScoringPossibility>
)

class DataConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromDiseaseList(value: List<Disease>): String = json.encodeToString(value)
    @TypeConverter
    fun toDiseaseList(value: String): List<Disease> = json.decodeFromString(value)

    @TypeConverter
    fun fromImmunoResonList(value: List<ImmunoReson>): String = json.encodeToString(value)
    @TypeConverter
    fun toImmunoResonList(value: String): List<ImmunoReson> = json.decodeFromString(value)

    @TypeConverter
    fun fromIndwellingDeviceList(value: List<IndwellingDevice>): String = json.encodeToString(value)
    @TypeConverter
    fun toIndwellingDeviceList(value: String): List<IndwellingDevice> = json.decodeFromString(value)

    @TypeConverter
    fun fromSeasonalityList(value: List<Seasonality>): String = json.encodeToString(value)
    @TypeConverter
    fun toSeasonalityList(value: String): List<Seasonality> = json.decodeFromString(value)

    @TypeConverter
    fun fromSignList(value: List<Sign>): String = json.encodeToString(value)
    @TypeConverter
    fun toSignList(value: String): List<Sign> = json.decodeFromString(value)

    @TypeConverter
    fun fromSymptomXList(value: List<SymptomX>): String = json.encodeToString(value)
    @TypeConverter
    fun toSymptomXList(value: String): List<SymptomX> = json.decodeFromString(value)

    @TypeConverter
    fun fromDiseaseXList(value: List<DiseaseX>): String = json.encodeToString(value)
    @TypeConverter
    fun toDiseaseXList(value: String): List<DiseaseX> = json.decodeFromString(value)

    @TypeConverter
    fun fromDiseaseIdenticifationlistsList(value: List<DiseaseIdenticifationlists>): String = json.encodeToString(value)
    @TypeConverter
    fun toDiseaseIdenticifationlistsList(value: String): List<DiseaseIdenticifationlists> = json.decodeFromString(value)

    @TypeConverter
    fun fromDiseaseIdenticifationlistsXList(value: List<DiseaseIdenticifationlistsX>): String = json.encodeToString(value)
    @TypeConverter
    fun toDiseaseIdenticifationlistsXList(value: String): List<DiseaseIdenticifationlistsX> = json.decodeFromString(value)

    @TypeConverter
    fun fromDiseaseSignlistList(value: List<DiseaseSignlist>): String = json.encodeToString(value)
    @TypeConverter
    fun toDiseaseSignlistList(value: String): List<DiseaseSignlist> = json.decodeFromString(value)

    @TypeConverter
    fun fromEtilogicalAgentList(value: List<EtilogicalAgent>): String = json.encodeToString(value)
    @TypeConverter
    fun toEtilogicalAgentList(value: String): List<EtilogicalAgent> = json.decodeFromString(value)

    @TypeConverter
    fun fromSymptomXXList(value: List<SymptomXX>): String = json.encodeToString(value)
    @TypeConverter
    fun toSymptomXXList(value: String): List<SymptomXX> = json.decodeFromString(value)

    @TypeConverter
    fun fromSyndromeTestList(value: List<SyndromeTest>): String = json.encodeToString(value)
    @TypeConverter
    fun toSyndromeTestList(value: String): List<SyndromeTest> = json.decodeFromString(value)

    @TypeConverter
    fun fromSyndromeXList(value: List<SyndromeX>): String = json.encodeToString(value)
    @TypeConverter
    fun toSyndromeXList(value: String): List<SyndromeX> = json.decodeFromString(value)

    @TypeConverter
    fun fromIVtoPOList(value: List<IVtoPO>): String = json.encodeToString(value)
    @TypeConverter
    fun toIVtoPOList(value: String): List<IVtoPO> = json.decodeFromString(value)

    @TypeConverter
    fun fromIsolationList(value: List<Isolation>): String = json.encodeToString(value)
    @TypeConverter
    fun toIsolationList(value: String): List<Isolation> = json.decodeFromString(value)

    @TypeConverter
    fun fromAntibioticDoseList(value: List<AntibioticDose>): String = json.encodeToString(value)
    @TypeConverter
    fun toAntibioticDoseList(value: String): List<AntibioticDose> = json.decodeFromString(value)

    @TypeConverter
    fun fromRenalFunctionCategoryList(value: List<RenalFunctionCategory>): String = json.encodeToString(value)
    @TypeConverter
    fun toRenalFunctionCategoryList(value: String): List<RenalFunctionCategory> = json.decodeFromString(value)

    @TypeConverter
    fun fromAntibioticGeneDtosList(value: List<AntibioticGeneListDto>): String = json.encodeToString(value)
    @TypeConverter
    fun toAntibioticGeneDtosList(value: String): List<AntibioticGeneListDto> = json.decodeFromString(value)

    @TypeConverter
    fun fromExposureProPhylaxisList(value: List<ExposureProPhylaxis>): String = json.encodeToString(value)
    @TypeConverter
    fun toExposureProPhylaxisList(value: String): List<ExposureProPhylaxis> = json.decodeFromString(value)

    @TypeConverter
    fun fromGridListsList(value: List<GridLists>): String = json.encodeToString(value)
    @TypeConverter
    fun toGridListsList(value: String): List<GridLists> = json.decodeFromString(value)

    @TypeConverter
    fun fromHivArtCenterList(value: List<HivArtCenter>): String = json.encodeToString(value)
    @TypeConverter
    fun toHivArtCenterList(value: String): List<HivArtCenter> = json.decodeFromString(value)

    @TypeConverter
    fun fromCultureTherapyGuidelistDtosList(value: List<CultureTherapyGuideDto>): String = json.encodeToString(value)
    @TypeConverter
    fun toCultureTherapyGuidelistDtosList(value: String): List<CultureTherapyGuideDto> = json.decodeFromString(value)

    @TypeConverter
    fun fromQSofaScoringPossibilityList(value: List<QSofaScoringPossibility>): String = json.encodeToString(value)
    @TypeConverter
    fun toQSofaScoringPossibilityList(value: String): List<QSofaScoringPossibility> = json.decodeFromString(value)

    @TypeConverter
    fun fromNews2ScoringPossibilityList(value: List<News2ScoringPossibility>): String = json.encodeToString(value)
    @TypeConverter
    fun toNews2ScoringPossibilityList(value: String): List<News2ScoringPossibility> = json.decodeFromString(value)
}
