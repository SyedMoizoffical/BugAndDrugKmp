package com.medical.buganddrug.AppModule

import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import com.medical.buganddrug.data.repository.UserRepository
import com.medical.buganddrug.ui.AntimicrobialSpectrumScreen.AntimicrobialSpectrumViewModel
import com.medical.buganddrug.ui.CultureGuideTherapy.CultureGuideViewModel
import com.medical.buganddrug.ui.EtiologicalAgentScreen.EtiologicalAgentScreenViewModel
import com.medical.buganddrug.ui.HIV.HivCenterScreen.HivArtCenterViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q1.QuestionViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q2.QuestionTwoViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q3.QuestionThreeViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionEightViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionFourViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.AppSurveyViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionFiveViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixViewModel
import com.medical.buganddrug.ui.clinicalSyndrome.ClinicalSyndromeViewModel
import com.medical.buganddrug.ui.onboarding.LogoutScreen.LogoutViewModel
import com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen.BugReportViewModel
import com.medical.buganddrug.ui.onboarding.loginScreen.AuthViewModel
import com.medical.buganddrug.ui.patientInfoScreen.UserViewModel
import com.medical.buganddrug.ui.postExposureProphylaxis.PostExposureProphylaxisViewModel
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val appModule = module {
    // Provide Settings with custom name
    single<Settings> {
        Settings()   // ← Pass the name here
    }
    single {
        SharedPreferenceManager(get())
    }

    // Repository
    single {
        QuestionsRepository(get())
    }
    // Repository
    single {
        UserRepository(get())
    }
    // ViewModels
// ViewModels
    factory {
        AuthViewModel(
            repository = get(),
            sharedPrefs = get()
        )
    }

    factory { ClinicalSyndromeViewModel(
        repository = get(),
        sharedPrefs = get()
    ) }
    factory { EtiologicalAgentScreenViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionTwoViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionThreeViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionFourViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionFiveViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionSixViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { QuestionEightViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { CultureGuideViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { AntimicrobialSpectrumViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { PostExposureProphylaxisViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { UserViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { AppSurveyViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { BugReportViewModel( 
        repository = get(),
        sharedPrefs = get()) }
    factory { LogoutViewModel(
        sharedPrefs = get()) }
    factory { HivArtCenterViewModel(
        repository = get()) }
}