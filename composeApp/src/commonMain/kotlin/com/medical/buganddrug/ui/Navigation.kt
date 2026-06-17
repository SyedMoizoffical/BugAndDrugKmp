package com.medical.buganddrug.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.*
import cafe.adriel.voyager.navigator.LocalNavigator
import com.medical.buganddrug.ui.AntimicrobialSpectrumScreen.AntimicrobialSpectrumViewModel
import com.medical.buganddrug.ui.CultureGuideTherapy.CultureGuideTherapyScreen
import com.medical.buganddrug.ui.CultureGuideTherapy.CultureGuideViewModel
import com.medical.buganddrug.ui.EtiologicalAgentScreen.EtiologicalAgentScreen
import com.medical.buganddrug.ui.EtiologicalAgentScreen.EtiologicalAgentScreenViewModel
import com.medical.buganddrug.ui.HIV.ClinicalGuideScreen
import com.medical.buganddrug.ui.HIV.HivCenterScreen.HivArtCenterViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q1.QuestionOneScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q1.QuestionViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q2.QuestionTwoViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q3.QuestionThreeScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q3.QuestionThreeViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionEightScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionEightViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionFourScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionFourViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.AppSurveyViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionFiveScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionFiveViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q7.QuestionSampleCollectionScreen
import com.medical.buganddrug.ui.Survey.PostAppSurveyScreen
import com.medical.buganddrug.ui.Survey.PreAppTrainingSurveyScreen
import com.medical.buganddrug.ui.VaccineIndication.VaccineIndicationScreen
import com.medical.buganddrug.ui.antimicrobial.AntimicrobialSpectrumScreen
import com.medical.buganddrug.ui.clinicalSyndrome.ClinicalSyndromeScreen
import com.medical.buganddrug.ui.clinicalSyndrome.ClinicalSyndromeViewModel
import com.medical.buganddrug.ui.onboarding.InpatientQuestion
import com.medical.buganddrug.ui.onboarding.NavDrawer.DisclaimerScreen
import com.medical.buganddrug.ui.onboarding.NavDrawer.LogoutScreen
import com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen.BugReportViewModel
import com.medical.buganddrug.ui.onboarding.OutpatientQuestion
import com.medical.buganddrug.ui.onboarding.PatientTypeSelectionScreen
import com.medical.buganddrug.ui.onboarding.WelcomeScreen
import com.medical.buganddrug.ui.onboarding.loginScreen.AuthViewModel
import com.medical.buganddrug.ui.patientInfoScreen.QuestionTwoScreen
import com.medical.buganddrug.ui.postExposureProphylaxis.PostExposureProphylaxisScreen
import com.medical.buganddrug.ui.postExposureProphylaxis.PostExposureProphylaxisViewModel
import org.koin.compose.koinInject
import com.medical.buganddrug.ui.QuickIDConsult.QuickIDConsults
import com.medical.buganddrug.ui.onboarding.LogoutScreen.LogoutViewModel
import com.medical.buganddrug.ui.patientInfoScreen.PatientInfoInputScreen
import com.medical.buganddrug.ui.patientInfoScreen.UserViewModel
import com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen.BugReportScreen

object AppScreens {

    // ---------------- ROOT ----------------
    @Composable
    fun App() {
        Navigator(Welcome)
    }
    

    object Welcome : Screen {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow

            val viewModel: AuthViewModel = koinInject()

            WelcomeScreen(
                viewModel,
                onNavigateToSignIn = {
                    navigator?.push(AppScreens.PatientTypeSelection)
                }
            )
        }
    }

    // ---------------- PATIENT TYPE ----------------
    object PatientTypeSelection : Screen {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow

            PatientTypeSelectionScreen(
                onPatientInfoClick = { navigator?.push(PatientDetail)},
                onPostSurveyClick = { navigator?.push(PostSurvey) },
                onPreSurveyClick = { navigator?.push(PreSurvey) },
                onInPatientTypeClick = { navigator?.push(InPatientQuestion) },
                onOutPatientTypeClick = { navigator?.push(OutPatientQuestion) },
                onBackClick = { navigator?.pop() },
                onBugReportClick = { navigator?.push(BugReport) },
                onPrivacyPolicyClick = { navigator?.push(Disclaimer) },
                onLogoutClick = { navigator?.push(Logout) }
            )
        }
    }

    // ---------------- INPATIENT ----------------
    object InPatientQuestion : Screen {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            var showComingSoon by remember { mutableStateOf(false) }

            if (showComingSoon) {
                AlertDialog(
                    onDismissRequest = { showComingSoon = false },
                    title = { Text("Coming Soon") },
                    text = { Text("This feature will be available soon.") },
                    confirmButton = {
                        TextButton({ showComingSoon = false }) { Text("OK") }
                    }
                )
            }

            InpatientQuestion(
                onPatientTypeClick = { topicId, patientType ->
                    when (topicId) {
                        "1" -> navigator?.push(QuickIDConsultScreen(topicId, patientType))
                        "2" -> navigator?.push(ClinicalSyndrome(patientType))
                        "3" -> navigator?.push(EtiologicalAgent(patientType))
                        "4" -> navigator?.push(QuestionSix)
                        "5" -> navigator?.push(Antimicrobial(patientType))
                        "6" -> navigator?.push(QuestionFive)
                        "7" -> navigator?.push(Hiv(patientType))
                        "8" -> navigator?.push(Vaccine(patientType))
                        else -> showComingSoon = true
                    }
                },
                onBackClick = { navigator?.pop() }
            )
        }
    }

    // ---------------- OUTPATIENT ----------------
    object OutPatientQuestion : Screen {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            var showComingSoon by remember { mutableStateOf(false) }

            OutpatientQuestion(
                onPatientTypeClick = { topicId, patientType ->
                    when (topicId) {
                        "1" -> navigator?.push(ClinicalSyndrome(patientType))
                        "2" -> navigator?.push(EtiologicalAgent(patientType))
                        "3" -> navigator?.push(QuestionSix)
                        "4" -> navigator?.push(QuestionFive)
                        "5" -> navigator?.push(Hiv(patientType))
                        "6" -> navigator?.push(PostExposure(patientType))
                        "7" -> navigator?.push(Vaccine(patientType))
                        else -> showComingSoon = true
                    }
                },
                onBackClick = { navigator?.pop() }
            )
        }
    }

    // ---------------- QUICK ID ----------------
    class QuickIDConsultScreen(
        private val topic: String,
        private val patientType: String
    ) : Screen {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow

            QuickIDConsults(
                topic = "Quick ID Consult",
                patientType = patientType,
                onQuestionClick = {
                    when (it) {
                        "1" -> navigator?.push(QuestionOne(patientType))
                        "2" -> navigator?.push(QuestionTwo)
                        "3" -> navigator?.push(QuestionThree)
                        "4" -> navigator?.push(QuestionFour)
                        "5" -> navigator?.push(QuestionSeven)
                        "6" -> navigator?.push(QuestionNine)
                        "7" -> navigator?.push(QuestionEight)
                        "8" -> navigator?.push(PostExposure(patientType))
                    }
                },
                onBackClick = { navigator?.pop() }
            )
        }
    }

    // ---------------- QUESTIONS ----------------
    class QuestionOne(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionViewModel = koinInject()

            QuestionOneScreen(vm, {navigator?.pop() },  patientType,{ navigator?.pop() })
        }
    }

    object QuestionTwo : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionTwoViewModel = koinInject()
            QuestionTwoScreen(vm, {}, { navigator?.pop() })
        }
    }

    object QuestionThree : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionThreeViewModel = koinInject()
            QuestionThreeScreen(vm, {}, { navigator?.pop() })
        }
    }

    object QuestionFour : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionFourViewModel = koinInject()
            QuestionFourScreen(vm, {}, { navigator?.pop() })
        }
    }

    object QuestionFive : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionFiveViewModel = koinInject()
            QuestionFiveScreen(vm, {}, { navigator?.pop() })
        }
    }

    object QuestionSix : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionSixViewModel = koinInject()
            QuestionSixScreen(vm, {}, { navigator?.pop() })
        }
    }

    object QuestionSeven : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            QuestionSampleCollectionScreen { navigator?.pop() }
        }
    }

    object QuestionEight : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionEightViewModel = koinInject()
            QuestionEightScreen(vm, {}, { navigator?.pop() })
        }
    }

    object QuestionNine : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: CultureGuideViewModel = koinInject()
            CultureGuideTherapyScreen(vm, {}, { navigator?.pop() })
        }
    }

    // ---------------- CORE SCREENS ----------------
    class ClinicalSyndrome(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: ClinicalSyndromeViewModel = koinInject()
            ClinicalSyndromeScreen(vm) { navigator?.pop() }
        }
    }

    class EtiologicalAgent(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: EtiologicalAgentScreenViewModel = koinInject()
            EtiologicalAgentScreen(vm) { navigator?.pop() }
        }
    }

    class Antimicrobial(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: AntimicrobialSpectrumViewModel = koinInject()
            AntimicrobialSpectrumScreen(vm, {navigator?.pop() },  patientType,{ navigator?.pop() },)
        }
    }

    class Hiv(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: HivArtCenterViewModel = koinInject()

            ClinicalGuideScreen(vm) { navigator?.pop() }
        }
    }

    class Vaccine(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: QuestionFourViewModel = koinInject()
            VaccineIndicationScreen(vm) { navigator?.pop() }
        }
    }

    class PostExposure(private val patientType: String) : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: PostExposureProphylaxisViewModel = koinInject()
            PostExposureProphylaxisScreen(vm) { navigator?.pop() }
        }
    }

    // ---------------- OTHER ----------------
    object PatientDetail : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: UserViewModel = koinInject()
            PatientInfoInputScreen(vm, { navigator?.pop() }, { navigator?.pop() })
        }
    }

    object PostSurvey : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: AppSurveyViewModel = koinInject()
            PostAppSurveyScreen(vm,{}, { navigator?.pop() })
        }
    }

    object PreSurvey : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: AppSurveyViewModel = koinInject()
            PreAppTrainingSurveyScreen(vm, {}, { navigator?.pop() })
        }
    }

    object Disclaimer : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            DisclaimerScreen { navigator?.pop() }
        }
    }

    object BugReport : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: BugReportViewModel = koinInject()
            BugReportScreen({ navigator?.pop() },vm)
        }
    }

    object Logout : Screen {
        @Composable override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val vm: LogoutViewModel = koinInject()

            LogoutScreen(
                vm,
                onBackClick = { navigator?.pop() },
                onConfirmLogout = {
                    navigator?.replaceAll(Welcome)
                }
            )
        }
    }
}