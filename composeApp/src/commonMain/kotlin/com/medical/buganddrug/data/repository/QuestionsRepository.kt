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

class QuestionsRepository (

    private val api: ApiService
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

