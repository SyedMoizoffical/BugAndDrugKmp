package com.medical.buganddrug.data.repository

import com.medical.buganddrug.data.model.AntimicrobialSpectrumData.AntimicrobialSpectrumModel
import com.medical.buganddrug.data.model.ApiResponse
import com.medical.buganddrug.data.model.CultureTherapyGuideModel.CultureTherapyGuideModel
import com.medical.buganddrug.data.model.QoestionsModel.Q1Model.NewsPostRequest
import com.medical.buganddrug.data.model.QoestionsModel.Q1Model.QsofaNewsResponse
import com.medical.buganddrug.data.model.QoestionsModel.Q1QSofaRequestModel
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.ClinicalSyndromeResponseModel
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.QuestionTwoResponseModel
import com.medical.buganddrug.data.model.QoestionsModel.Q3Model.GetIVtoPOsData
import com.medical.buganddrug.data.model.QoestionsModel.Q4Model.GetPrecautionFinderList
import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.Q5Response
import com.medical.buganddrug.data.model.QoestionsModel.Q8Model.GetAntibioticGeneListResponse
import com.medical.buganddrug.data.model.Survey.AppSurveyPostRequest
import com.medical.buganddrug.data.model.hivCenterModel.HivArtCenter
import com.medical.buganddrug.data.model.patientinfoModel.Data
import com.medical.buganddrug.data.model.postExosureProplaxisModel.ExposureProPhylaxisModel
import com.medical.buganddrug.data.remote.ApiService
import com.medical.buganddrug.ui.onboarding.loginScreen.SignUpResponseDataModel
import com.medical.buganddrug.data.local.*
import com.medical.buganddrug.data.model.LocalStorageDatamodel.*

class QuestionsRepository (

    private val api: ApiService,
    private val localDao: LocalDataDao
) {
    suspend fun getUser(): Result<Data?> {
        return try {
            val response = api.getUser()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
//Question One
    suspend fun submitQ1QSofa(q1QSofaRequestModel: Q1QSofaRequestModel): Result<QsofaNewsResponse?> {
        return try {
            val response = api.submitQ1QSofa(q1QSofaRequestModel)
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun submitQ1News2(q1QSofaRequestModel: NewsPostRequest): Result<QsofaNewsResponse?> {
        return try {
            val response = api.submitQ1News2(q1QSofaRequestModel)
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Question Two
    suspend fun getQ2Data(): Result<QuestionTwoResponseModel?> {
        return try {
            val response = api.getQ2Data()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }   //Question Two
    suspend fun getClinicalSyndromeData(): Result<ClinicalSyndromeResponseModel?> {
        return try {
            val response = api.getClinicalSyndromeData()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Question Three
    suspend fun getQ3Data(): Result<GetIVtoPOsData?> {
        return try {
            val response = api.getIVtoPOsData()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Four
    suspend fun getQ4Data(): Result<GetPrecautionFinderList?> {
        return try {

            val response = api.getPrecautionFinderList()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Five
    suspend fun getQ5Data(): Result<Q5Response?> {
        return try {
            val response = api.getCreatinineClearance()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }    //Question six
    suspend fun getAntibiotic(): Result<Q5Response?> {
        return try {
            val response = api.getAntibiotic()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Eight
    suspend fun getQ8Data(): Result<GetAntibioticGeneListResponse?> {
        return try {
            val response = api.getAntibioticGeneList()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Four
    suspend fun getExposureProPhylaxisModel(): Result<ExposureProPhylaxisModel?> {
        return try {
            val response = api.getExposureProPhylaxisModelData()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Two
    suspend fun getAntimicrobialSpectrumData(): Result<AntimicrobialSpectrumModel?> {
        return try {
            val response = api.getAntimicrobialSpectrumData()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAntibioticForSurvey(): Result<Q5Response?> {
        return try {
            val response = api.getAntibiotic()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun submitSurvey(request: AppSurveyPostRequest): Result<ApiResponse<Unit>> {
        return try {
            val response = api.postAppSurvey(request)
            if (response.statusCode == 200) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
    suspend fun fetchCenters(): Result<List<HivArtCenter>> {
        return try {
            val response = api.getHivArtCenters()
            if (response.statusCode == 200) {
                Result.success(response.data!!.hivArtCenters)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//login
suspend fun checkEmailExists(email: String): Result<ApiResponse<SignUpResponseDataModel>> {
    return try {
        val response = api.checkEmail(email) // GET SignIn?email=...
        Result.success(response)
        // Adjust according to your actual response model
    } catch (e: Exception) {
        Result.failure(e)
    }
}

    suspend fun signUp(name: String, email: String, password: String, pmdc: String): Result<ApiResponse<SignUpResponseDataModel>> {
        return try {
            val response = api.signUp(
                mapOf(
                    "name"     to name,
                    "email"    to email,
                    "password" to password,
                    "pmdc"     to pmdc
                )
            )
            if (response.statusCode == 200) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.msg ?: "Sign up failed with status ${response.statusCode}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Two
    suspend fun getCultureGuideApi(): Result<CultureTherapyGuideModel?> {
        return try {
            val response = api.getCultureGuideApi()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Four
    suspend fun getEtiologicalAgent(): Result<QuestionTwoResponseModel?> {
        return try {
            val response = api.getEtiologicalAgent()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //Question Four
    suspend fun getAllLocalData(): Result<LocalDataModel?> {
        return try {
            val response = api.getAllDataForLocal()
            if (response.statusCode == 200) {
                val localData = response.data
                if (localData != null) {
                    localDao.insertLovs(
                        LovsEntity(
                            diseases = localData.lovs!!.diseases,
                            immunoReson = localData.lovs.immunoReson,
                            indwellingDevices = localData.lovs.indwellingDevices,
                            seasonalities = localData.lovs.seasonalities,
                            sign = localData.lovs.sign,
                            symptoms = localData.lovs.symptoms
                        )
                    )
                    localDao.insertSyndromeIdentificationDataQ2(
                        SyndromeIdentificationDataQ2Entity(
                            disease = localData.syndromeIdentificationDataQ2!!.disease!!,
                            diseaseIdenticifationlists = localData.syndromeIdentificationDataQ2!!.diseaseIdenticifationlists!!,
                            diseaseSignlist = localData.syndromeIdentificationDataQ2.diseaseSignlist!!,
                            etilogicalAgents = localData.syndromeIdentificationDataQ2.etilogicalAgents!!,
                            symptom = localData.syndromeIdentificationDataQ2.symptom!!,
                            syndromeTests = localData.syndromeIdentificationDataQ2.syndromeTests!!,
                            syndromes = localData.syndromeIdentificationDataQ2.syndromes!!
                        )
                    )
                    localDao.insertSyndromeIdentificationData(
                        SyndromeIdentificationDataEntity(
                            disease = localData.syndromeIdentificationData!!.disease!!,
                            diseaseIdenticifationlists = localData.syndromeIdentificationData.diseaseIdenticifationlists!!,
                            diseaseSignlist = localData.syndromeIdentificationData.diseaseSignlist!!,
                            etilogicalAgents = localData.syndromeIdentificationData.etilogicalAgents!!,
                            symptom = localData.syndromeIdentificationData.symptom!!,
                            syndromeTests = localData.syndromeIdentificationData.syndromeTests!!,
                            syndromes = localData.syndromeIdentificationData.syndromes!!
                        )
                    )
                    localDao.insertIvToPOs(
                        IvToPOsEntity(
                            iVtoPOs = localData.ivToPOs!!.iVtoPOs!!,
                            url = localData.ivToPOs.url!!
                        )
                    )
                    localDao.insertPrecautionFinderList(
                        PrecautionFinderListEntity(
                            isolations = localData.precautionFinderList!!.isolations!!
                        )
                    )
                    localDao.insertCreatinineClearance(
                        CreatinineClearanceEntity(
                            antibioticDoses = localData.creatinineClearance!!.antibioticDoses!!,
                            renalFunctionCategories = localData.creatinineClearance.renalFunctionCategories!!
                        )
                    )
                    localDao.insertAntibioticGeneList(
                        AntibioticGeneListEntity(
                            antibioticGeneListDtos = localData.antibioticGeneList!!.antibioticGeneListDtos!!
                        )
                    )
                    localDao.insertExposureProPhylaxisList(
                        ExposureProPhylaxisListEntity(
                            exposureProPhylaxisList = localData.exposureProPhylaxisList!!.exposureProPhylaxisList!!
                        )
                    )
                    localDao.insertBacteriaSusceptibilityList(
                        BacteriaSusceptibilityListEntity(
                            gridLists = localData.bacteriaSusceptibilityList!!.gridLists!!
                        )
                    )
                    localDao.insertHivArtCenterList(
                        HivArtCenterListEntity(
                            hivArtCenters = localData.hivArtCenterList!!.hivArtCenters!!
                        )
                    )
                    localDao.insertCultureTherapyGuideList(
                        CultureTherapyGuideListEntity(
                            cultureTherapyGuidelistDtos = localData.cultureTherapyGuideList!!.cultureTherapyGuidelistDtos!!
                        )
                    )
                    localData.qSofaScoringPossibilities?.let { list ->
                        localDao.insertQSofaScoringPossibilities(
                            QSofaScoringPossibilitiesEntity(
                                qSofaScoringPossibilities = list
                            )
                        )
                    }
                    localData.news2ScoringPossibilities?.let { list ->
                        localDao.insertNews2ScoringPossibilities(
                            News2ScoringPossibilitiesEntity(
                                news2ScoringPossibilities = list
                            )
                        )
                    }
                }
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocalLovs(): Lovs? = localDao.getLovs()?.let {
        Lovs(
            diseases = it.diseases,
            immunoReson = it.immunoReson,
            indwellingDevices = it.indwellingDevices,
            seasonalities = it.seasonalities,
            sign = it.sign,
            symptoms = it.symptoms
        )
    }

    suspend fun getLocalSyndromeIdentificationDataQ2(): SyndromeIdentificationDataQ2? = localDao.getSyndromeIdentificationDataQ2()?.let {
        SyndromeIdentificationDataQ2(
            disease = it.disease,
            diseaseIdenticifationlists = it.diseaseIdenticifationlists,
            diseaseSignlist = it.diseaseSignlist,
            etilogicalAgents = it.etilogicalAgents,
            symptom = it.symptom,
            syndromeTests = it.syndromeTests,
            syndromes = it.syndromes
        )
    }

    suspend fun getLocalSyndromeIdentificationData(): SyndromeIdentificationData? = localDao.getSyndromeIdentificationData()?.let {
        SyndromeIdentificationData(
            disease = it.disease,
            diseaseIdenticifationlists = it.diseaseIdenticifationlists,
            diseaseSignlist = it.diseaseSignlist,
            etilogicalAgents = it.etilogicalAgents,
            symptom = it.symptom,
            syndromeTests = it.syndromeTests,
            syndromes = it.syndromes
        )
    }

    suspend fun getLocalIvToPOs(): IvToPOs? = localDao.getIvToPOs()?.let {
        IvToPOs(
            iVtoPOs = it.iVtoPOs,
            url = it.url
        )
    }

    suspend fun getLocalPrecautionFinderList(): GetPrecautionFinderList? = localDao.getPrecautionFinderList()?.let {
        GetPrecautionFinderList(
            isolations = it.isolations
        )
    }

    suspend fun getLocalCreatinineClearance(): CreatinineClearance? = localDao.getCreatinineClearance()?.let {
        CreatinineClearance(
            antibioticDoses = it.antibioticDoses,
            renalFunctionCategories = it.renalFunctionCategories
        )
    }

    suspend fun getLocalAntibioticGeneList(): AntibioticGeneListEntity? = localDao.getAntibioticGeneList()?.let {
        AntibioticGeneListEntity(
            antibioticGeneListDtos = it.antibioticGeneListDtos
        )
    }

    suspend fun getLocalExposureProPhylaxisList(): ExposureProPhylaxisList? = localDao.getExposureProPhylaxisList()?.let {
        ExposureProPhylaxisList(
            exposureProPhylaxisList = it.exposureProPhylaxisList
        )
    }

    suspend fun getLocalBacteriaSusceptibilityList(): BacteriaSusceptibilityList? = localDao.getBacteriaSusceptibilityList()?.let {
        BacteriaSusceptibilityList(
            gridLists = it.gridLists
        )
    }

    suspend fun getLocalHivArtCenterList(): HivArtCenterList? = localDao.getHivArtCenterList()?.let {
        HivArtCenterList(
            hivArtCenters = it.hivArtCenters
        )
    }

    suspend fun hasLocalData(): Boolean = localDao.getLovs() != null

    suspend fun getLocalCultureTherapyGuideList(): CultureTherapyGuideList? = localDao.getCultureTherapyGuideList()?.let {
        CultureTherapyGuideList(
            cultureTherapyGuidelistDtos = it.cultureTherapyGuidelistDtos
        )
    }

    suspend fun getLocalQSofaScoringPossibilities(): List<QSofaScoringPossibility>? = localDao.getQSofaScoringPossibilities()?.qSofaScoringPossibilities

    suspend fun getLocalNews2ScoringPossibilities(): List<News2ScoringPossibility>? = localDao.getNews2ScoringPossibilities()?.news2ScoringPossibilities

    /**
     * Get extracted list of DiseaseItem (name, id, type) from local database.
     * Fallback to default list if local database is empty.
     */
    suspend fun getDiseaseListFromLocalDatabase(): List<com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseItem> {
        val lovs = getLocalLovs()
        val syndromeData = getLocalSyndromeIdentificationData()
        val syndromeDataQ2 = getLocalSyndromeIdentificationDataQ2()
        val creatinineClearance = getLocalCreatinineClearance()
        return com.medical.buganddrug.util.DiseaseExtractor.getCombinedDiseaseList(
            lovs = lovs,
            syndromeData = syndromeData,
            syndromeDataQ2 = syndromeDataQ2,
            creatinineClearance = creatinineClearance
        )
    }

    suspend fun postBugReport(
        description: String,
        email: String,
        status: String,
        devicesDetail: String,
        images: List<ByteArray>
    ): Result<Int?> {
        return try {

            val response = api.postBugReport(
                description,
                email,
                status,
                devicesDetail,
                images
            )

            Result.success(response.statusCode)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


