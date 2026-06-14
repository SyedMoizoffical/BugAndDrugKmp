package com.medical.buganddrug.data.remote

import com.medical.buganddrug.data.model.*
import com.medical.buganddrug.data.model.AntimicrobialSpectrumData.AntimicrobialSpectrumModel
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
import com.medical.buganddrug.data.model.hivCenterModel.HivArtCenterResponse
import com.medical.buganddrug.data.model.patientinfoModel.Data
import com.medical.buganddrug.data.model.postExosureProplaxisModel.ExposureProPhylaxisModel
import com.medical.buganddrug.ui.onboarding.loginScreen.SignUpResponseDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.*

class ApiService(
    private val client: HttpClient
) {

    suspend fun getUser(): ApiResponse<Data> {
        return client.post("GetAllLovs").body()
    }

    suspend fun submitPatientInfo(patientInfo: PatientInfo): ApiResponse<Unit> {
        return client.post("AddPaitentInfo") {
            contentType(ContentType.Application.Json)
            setBody(patientInfo)
        }.body()
    }

    // Q1 - QSOFA
    suspend fun submitQ1QSofa(req: Q1QSofaRequestModel): ApiResponse<QsofaNewsResponse> {
        return client.post("AddQSofa") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body()
    }

    suspend fun submitQ1News2(req: NewsPostRequest): ApiResponse<QsofaNewsResponse> {
        return client.post("AddNews2Score") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.body()
    }

    // Q2
    suspend fun getQ2Data(): ApiResponse<QuestionTwoResponseModel> {
        return client.post("GetSyndromeIdentificationDataQ2").body()
    }

    suspend fun getClinicalSyndromeData(): ApiResponse<ClinicalSyndromeResponseModel> {
        return client.post("GetSyndromeIdentificationData").body()
    }

    // Q3
    suspend fun getIVtoPOsData(): ApiResponse<GetIVtoPOsData> {
        return client.post("GetIVtoPOsData").body()
    }

    // Q4
    suspend fun getPrecautionFinderList(): ApiResponse<GetPrecautionFinderList> {
        return client.post("GetPrecautionFinderList").body()
    }

    // Q5
    suspend fun getCreatinineClearance(): ApiResponse<Q5Response> {
        return client.post("GetCreatinineClearance").body()
    }

    suspend fun getAntibiotic(): ApiResponse<Q5Response> {
        return client.post("GetCreatinineClearance").body()
    }

    // Survey
    suspend fun postAppSurvey(request: AppSurveyPostRequest): ApiResponse<Unit> {
        return client.post("PostAppSurvey") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // Q8
    suspend fun getAntibioticGeneList(): ApiResponse<GetAntibioticGeneListResponse> {
        return client.post("GetAntibioticGeneList").body()
    }

    // Exposure
    suspend fun getExposureProPhylaxisModelData(): ApiResponse<ExposureProPhylaxisModel> {
        return client.post("GetExposureProPhylaxisList").body()
    }

    // Antimicrobial
    suspend fun getAntimicrobialSpectrumData(): ApiResponse<AntimicrobialSpectrumModel> {
        return client.post("GetBacteriaSusceptibilityList").body()
    }

    // HIV
    suspend fun getHivArtCenters(): ApiResponse<HivArtCenterResponse> {
        return client.post("GetHIVArtCenterList").body()
    }

    // Login
    suspend fun checkEmail(email: String): ApiResponse<SignUpResponseDataModel> {
        return client.post("SignIn") {
            parameter("email", email)
        }.body()
    }

    suspend fun signUp(body: Map<String, String>): ApiResponse<SignUpResponseDataModel> {
        return client.post("SignUp") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    // Culture guide
    suspend fun getCultureGuideApi(): ApiResponse<CultureTherapyGuideModel> {
        return client.post("GetCultureTherapyGuideList").body()
    }
    // Culture guide
    suspend fun getEtiologicalAgent(): ApiResponse<QuestionTwoResponseModel> {
        return client.post("GetSyndromeIdentificationData").body()
    }



    suspend fun postBugReport(
        description: String,
        email: String,
        status: String,
        devicesDetail: String,
        images: List<ByteArray>
    ): ApiResponse<Nothing> {

        return client.submitFormWithBinaryData(
            url = "PostBugReport",
            formData = formData {
                append("description", description)
                append("email", email)
                append("status", status)
                append("devicesDetail", devicesDetail)
//
//                images.forEachIndexed { index, bytes ->
//                    append(
//                        key = "images",
//                        value = bytes,
//                        headers = Headers.build {
//                            append(
//                                HttpHeaders.ContentDisposition,
//                                "filename=image_$index.jpg"
//                            )
//                        }
//                    )
//                }
            }
        ).body()
    }
}