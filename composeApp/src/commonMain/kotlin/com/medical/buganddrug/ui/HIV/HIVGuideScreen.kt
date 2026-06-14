package com.medical.buganddrug.ui.HIV

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.medical.buganddrug.ui.HIV.HivCenterScreen.ArtResistanceTreatmentScreen
import com.medical.buganddrug.ui.HIV.HivCenterScreen.ArtSideEffectsScreen
import com.medical.buganddrug.ui.HIV.HivCenterScreen.CenterScreen
import com.medical.buganddrug.ui.HIV.HivCenterScreen.HivArtCenterViewModel
import com.medical.buganddrug.ui.HIV.HivCenterScreen.OpportunisticInfectionScreen
import org.koin.compose.koinInject

// -------------------- HIV DATA --------------------
//val hivQuestions = listOf(
//
//    QuestionItem(
//        1,
//        title = "1. Whom to test for HIV",
//        details = """
//1. People with Clinical Indicators
//· Anyone with symptoms or signs suggestive of:
//. Acute HIV infection (fever, rash, lymphadenopathy)
//. Opportunistic infections (TB, recurrent pneumonia, unexplained weight loss)
//. Sexually transmitted infections (STIs)
//· People diagnosed with Tuberculosis or TB symptoms
//· Pregnant women (as part of antenatal care)
//
//2. Key Populations at Increased Risk
//· People who inject drugs (PWID)
//· Transgender individuals
//· Men who have sex with men (MSM)
//· Female sex workers
//· Clients of sex workers
//
//3. Partners and Contacts of People with HIV
//· Sexual partners of known HIV-positive individuals
//· Children born to mothers with HIV (early infant diagnosis)
//
//4. Occupational Exposure
//· Healthcare workers after a potential occupational exposure (needle stick or contact with infectious fluids)
//"""
//    ),
//
//    QuestionItem(
//        2,
//        title = "2. Clinical Staging of HIV (WHO Classification)",
//        details = """
//WHO Clinical Stage 1
//• Asymptomatic
//• Persistent generalized lymphadenopathy
//
//WHO Clinical Stage 2
//• Moderate unexplained weight loss (<10% body weight)
//• Recurrent respiratory infections (sinusitis, tonsillitis, otitis media, pharyngitis)
//• Herpes zoster (shingles)
//• Recurrent oral ulcerations
//• Papular pruritic eruptions
//• Seborrheic dermatitis
//• Fungal nail infections
//
//WHO Clinical Stage 3
//• Unexplained severe weight loss (>10% body weight)
//• Unexplained chronic diarrhea >1 month
//• Unexplained persistent fever >1 month
//• Persistent oral candidiasis
//• Oral hairy leukoplakia
//• Pulmonary TB (current)
//• Severe bacterial infections (pneumonia, empyema, meningitis, etc.)
//• Acute necrotizing ulcerative gingivitis/periodontitis
//• Unexplained anemia (<8 g/dL), neutropenia, or thrombocytopenia
//
//WHO Clinical Stage 4 (AIDS-defining)
//• HIV wasting syndrome
//• Pneumocystis jerovecii pneumonia (PJP)
//• Recurrent severe bacterial pneumonia
//• Chronic herpes simplex infection (>1 month)
//• Esophageal candidiasis
//• Extra-pulmonary TB
//• Kaposi sarcoma
//• Cytomegalovirus disease (retinitis, etc.)
//• CNS toxoplasmosis
//• HIV encephalopathy
//• Extrapulmonary cryptococcosis
//• Disseminated Mycobacterium avium complex
//• Disseminated fungal infections (histoplasmosis, coccidioidomycosis)
//• Recurrent septicemia
//• Lymphoma (cerebral or non-Hodgkin)
//• Invasive cervical cancer
//• Atypical disseminated mycobacterial infection
//• Progressive multifocal leukoencephalopathy (PML)
//"""
//    ),
//
//    QuestionItem(
//        3,
//        title = "3. Baseline Screening Tests Before ART Initiation (National Guidelines)",
//        details = """
//• CBC/Haemoglobin
//• Serum creatinine
//• Liver function tests
//• Urine DR
//• Exclude TB
//• Symptoms
//• CXR
//• MT/PPD
//• Pregnancy test
//• Exclude other STI/Infections transmitted by similar mode
//• Syphilis (RPR/VDRL)
//• Hepatitis BsAg, AntiHCV
//• CD4 count
//• Check Serum Cryptococcal Antigen (If CD4 < 200)
//• Viral load is not checked in Pakistan at the time of initial diagnosis
//"""
//    ),
//
//    QuestionItem(
//        4,
//        title = "4. Baseline Screening Tests before ART Initiation (International Guidelines)",
//        details = """
//• CBC
//• UCE
//• LFT
//• FBS
//• FLP
//• Urine DR
//• Hepatitis A, Hepatitis B, Hepatitis C, Hepatitis F
//• Pregnancy test
//• PAP smear
//• Toxoplasma & CMV serology
//• CD4 count
//• Check Serum Cryptococcal Antigen (If CD4 < 100)
//• Viral load
//• If VL >1000: Viral genotype resistance assay
//• Exclude other Sexually Transmitted Infection (RPR/VDRL, NAAT for other STI)
//• HLAB5701 for Abacavir hypersensitivity reaction
//"""
//    ),
//
//    QuestionItem(
//        5,
//        title = "5. HIV treatment (Antiretroviral Therapy)",
//        details = """
//Types of Anti-retroviral medication
//Nucleotide Reverse Transcriptase Inhibitor (NRTI)
//Abacavir (ABC)
//Didanosine (ddI)
//Emtricitabine (FTC)
//Lamivudine (3TC)
//Stavudine (d4T)
//Tenofovir (TDF/TAF)
//Zidovudine (AZT)
//
//Non-Nucleoside Reverse Transcriptase Inhibitor (NNRTI)
//Delavirdine (DLV)
//Efavirenz (EFV)
//Etravirine (ETR)
//Nevirapine (NVP)
//Rilpivirine (RPV)
//
//Protease Inhibitors
//Atazanavir (ATV)
//Darunavir (DRV)
//Fosamprenavir (FPV)
//Indinavir (IDV)
//Lopinavir (LPV)
//Nelfinavir (NFV)
//Ritonavir (RTV)
//Saquinavir (SQV)
//Tipranavir (TPV)
//
//Integrase Inhibitor
//Raltegravir (RAL)
//Elvitegravir (EVG)
//Dolutegravir (DTG)
//Bictegravir (BIC)
//Cabotegravir (CAB)
//
//Fusion Inhibitor
//Enfuvirtide (ENF)
//
//CCR5 Antagonist
//Maraviroc (MVC)
//
//Attachment inhibitor
//Fostemsavir (FTR)
//
//Post-Attachment inhibitors
//Ibalizumab
//
//Capsid inhibitors
//Lenacapavir
//"""
//    ),
//
//    QuestionItem(
//        6,
//        title = "6. 5.In Which Co-Infections Initiation of ARV is delayed?",
//        details = """
//NRTI
//Abacavir (ABC)
//Lamivudine (3TC)
//Tenofovir (TDF/TAF)
//Zidovudine (AZT)
//
//NNRTI
//Efavirenz (EFV)
//Nevirapine (NVP)
//
//Protease Inhibitors
//Atazanavir (ATV)
//Lopinavir (LPV)
//Ritonavir (RTV)
//
//Integrase Inhibitor
//Raltegravir (RAL)
//Dolutegravir (DTG)
//"""
//    ),
//
//    QuestionItem(
//        7,
//        title = "7. Opportunistic Infection Prophylaxis",
//        details = """
//Adults: Combination therapy of 2 NRTI + INSTI or PI
//
//Preferred regimen:
//Dolutegravir/Lamivudine/Tenofovir (DTG/3TC/TDF)
//
//Regimen in renal dysfunction:
//Dolutegravir/Lamivudine/Zidovudine (DTG/3TC/AZT)
//
//Regimen in renal dysfunction and anemia:
//Dolutegravir/Lamivudine/Abacavir (DTG/3TC/ABC)
//
//Regimen in DTG psychosis:
//Atazanavir/ritonavir/Lamivudine/Tenofovir (ATVr/3TC/TDF)
//"""
//    ),
//
//    QuestionItem(
//        8,
//        title = "8. Monitoring on ART",
//        details = """
//Adults FDC
//Dolutegravir/Lamivudine/Tenofovir (50mg/300mg/300mg)
//Efavirenz/Lamivudine/Tenofovir (600mg/300mg/300mg)
//Efavirenz/Lamivudine/Zidovudine (600mg/150mg/300mg)
//Nevirapine/Lamivudine/Zidovudine (200mg/150mg/300mg)
//Lamivudine/Tenofovir (300mg/300mg)
//
//Paeds FDC
//Nevirapine/Lamivudine/Zidovudine (60mg/30mg/50mg)
//Abacavir/Lamivudine dispersible tablets (120mg/60mg)
//"""
//    ),
//
//    QuestionItem(
//        9,
//        title = "9. How to interpret viral load?",
//        details = """
//Tuberculosis
//If CD4 count < 50/mm3
//Start ARV within the first 2 weeks
//
//If CD4 count > 50/mm3
//Start ARV within the first 8 weeks
//
//TB meningitis: initiating ART 4-8 weeks after the start of TB treatment
//
//Cryptococcal infection
//Start ARV after 8 weeks of cryptococcal treatment
//"""
//    ),
//
//    QuestionItem(
//        10,
//        title = "10. How to manage virological failure?",
//        details = """
//Tuberculosis Preventive Therapy (TPT)
//Given when Tuberculin Skin Test (TST) is positive (5-10mm) or in all patients who screen negative for active TB by clinical history.
//
//Isoniazid 300mg once daily for 6-9 months
//
//Co-trimoxazole preventive therapy (CPT)
//Prevents Pneumocystis jirovecii
//Lowers the incidence of toxoplasma and bacterial infections
//
//Indication
//WHO clinical stage 3 or 4
//CD4 ≤350/mm3
//
//Discontinuation
//Clinically stable with CD4 ≥350/mm3 and/or
//Virologic suppression after 1 year of ART
//
//Dose
//Co-trimoxazole 800/160mg once daily
//"""
//    ),
//
//    QuestionItem(
//        11,
//        title = "11. ART Side Effects",
//        details = ""
//    ),
//
//    QuestionItem(
//        12,
//        title = "12. ART Resistance and Treatment Option",
//        details = ""
//    ),
//
//    QuestionItem(
//        13,
//        title = "13. HIV & Opportunistic Infections",
//        details = """
//Symptom clusters with associated opportunistic infections including TB, MAC, CMV, PJP, Cryptococcus, Toxoplasmosis, PML, CMV retinitis, Kaposi sarcoma, lymphoma, bacterial and fungal infections.
//"""
//    ),
//
//    QuestionItem(
//        14,
//        title = "14. Pre Exposure Prophylaxis (PrEP)",
//        details = """
//Given before exposure to prevent acquisition of HIV
//
//Combination therapy
//Tenofovir (TDF) + Lamivudine (3TC)
//
//Daily PrEP
//Start 7 days before exposure and continue until 28 days after exposure period
//
//Event driven PrEP (MSM)
//2 tablets 2–24 hours before exposure
//Then 1 tablet after exposure
//Then 1 tablet 24 hours later
//"""
//    ) ,
//    QuestionItem(
//        15,
//        title = "15. HIV Treatment Centres in Pakistan (Where to refer)",
//        details = ""
//    )
//)
val hivQuestions = listOf(

    QuestionItem(
        1,
        title = "1. Whom to test for HIV?",
        details = """
1. People with Clinical Indicators
.Anyone with symptoms or signs suggestive of:
.Acute HIV infection (fever, rash, lymphadenopathy)
.Opportunistic infections (TB, recurrent pneumonia, unexplained weight loss)
.Sexually transmitted infections (STIs)
.People diagnosed with Tuberculosis or TB symptoms
.Pregnant women (as part of antenatal care)

2. Key Populations at Increased Risk
.People who inject drugs (PWID)
.Transgender individuals
.Men who have sex with men (MSM)
.Female sex workers
.Clients of sex workers

3. Partners and Contacts of People with HIV
.Sexual partners of known HIV-positive individuals
.Children born to mothers with HIV (early infant diagnosis)

4. Occupational Exposure
.Healthcare workers after a potential occupational exposure (needle stick or contact with infectious fluids)
"""
    ),

    QuestionItem(
        2,
        title = "2. Clinical Staging of HIV (WHO Classification)",
        details = """
1. WHO Clinical Stage 1
• Asymptomatic
• Persistent generalized lymphadenopathy

2. WHO Clinical Stage 2
• Moderate unexplained weight loss (<10% body weight)
• Recurrent respiratory infections (sinusitis, tonsillitis, otitis media, pharyngitis)
• Herpes zoster (shingles)
• Recurrent oral ulcerations
• Papular pruritic eruptions
• Seborrheic dermatitis
• Fungal nail infections

3. WHO Clinical Stage 3
• Unexplained severe weight loss (>10% body weight)
• Unexplained chronic diarrhea >1 month
• Unexplained persistent fever >1 month
• Persistent oral candidiasis
• Oral hairy leukoplakia
• Pulmonary TB (current)
• Severe bacterial infections (pneumonia, empyema, meningitis, etc.)
• Acute necrotizing ulcerative gingivitis/periodontitis
• Unexplained anemia (<8 g/dL), neutropenia, or thrombocytopenia

4. WHO Clinical Stage 4 (AIDS-defining)
• HIV wasting syndrome
• Pneumocystis jerovecii pneumonia (PJP)
• Recurrent severe bacterial pneumonia
• Chronic herpes simplex infection (>1 month)
• Esophageal candidiasis
• Extra-pulmonary TB
• Kaposi sarcoma
• Cytomegalovirus disease (retinitis, etc.)
• CNS toxoplasmosis
• HIV encephalopathy
• Extrapulmonary cryptococcosis
• Disseminated Mycobacterium avium complex
• Disseminated fungal infections (histoplasmosis, coccidioidomycosis)
• Recurrent septicemia
• Lymphoma (cerebral or non-Hodgkin)
• Invasive cervical cancer
• Atypical disseminated mycobacterial infection
• Progressive multifocal leukoencephalopathy (PML)
"""
    ),

    QuestionItem(
        3,
        title = "3. Baseline Screening Tests Before ART Initiation",
        details = """
1. National Guidelines
•CBC/Haemoglobin
•Serum creatinine
•Liver function tests
•Urine DR
•Exclude TB
•Symptoms
•CXR
•MT/PPD
•Pregnancy test
•Exclude other STI/Infections transmitted by similar mode
•Syphilis (RPR/VDRL)
•Hepatitis BsAg, AntiHCV
•CD4 count
•Check Serum Cryptococcal Antigen (If CD4 < 200)
Viral load is not checked in Pakistan at the time of initial diagnosis

2. International Guidelines
.CBC
.UCE
.LFT
.FBS
.FLP
.Urine DR
.Hepatitis A, Hepatitis B, Hepatitis C, Hepatitis F
.Pregnancy test
.PAP smear
.Toxoplasma & CMV serology
.CD4 count
.Check Serum Cryptococcal Antigen (If CD4 < 100)
.Viral load
.If VL >1000: Viral genotype resistance assay
.Exclude other Sexually Transmitted Infection (RPR/VDRL, NAAT for other STI)
.HLAB5701 for Abacavir hypersensitivity reaction
"""
    ),

    QuestionItem(
        4,
        title = "4. HIV Treatment – Antiretroviral Therapy",
        details = """
Types of Anti-retroviral medication

1. Nucleotide Reverse Transcriptase Inhibitor (NRTI)
. Abacavir (ABC)
. Didanosine (ddI)
. Emtricitabine (FTC)
. Lamivudine (3TC)
. Stavudine (d4T)
. Tenofovir (TDF/TAF)
. Zidovudine (AZT)

2. Non-Nucleoside Reverse Transcriptase Inhibitor (NNRTI)
. Delavirdine (DLV)
. Efavirenz (EFV)
. Etravirine (ETR)
. Nevirapine (NVP)
. Rilpivirine (RPV)

3. Protease Inhibitors
. Atazanavir (ATV)
. Darunavir (DRV)
. Fosamprenavir (FPV)
. Indinavir (IDV)
. Lopinavir (LPV)
. Nelfinavir (NFV)
. Ritonavir (RTV)
. Saquinavir (SQV)
. Tipranavir (TPV)

4. Integrase Inhibitor
. Raltegravir (RAL)
. Elvitegravir (EVG)
. Dolutegravir (DTG)
. Bictegravir (BIC)
. Cabotegravir (CAB)

5. Fusion Inhibitor
. Enfuvirtide (ENF)

6. CCR5 Antagonist
. Maraviroc (MVC)

7. Attachment inhibitor
. Fostemsavir (FTR)

8. Post-Attachment inhibitors
. Ibalizumab

9. Capsid inhibitors
. Lenacapavir
"""
    ),
    QuestionItem(
        5,
        title = "5. Antiretroviral Medication Available in Pakistan",
        details = """

1. Nucleotide Reverse Transcriptase Inhibitor (NRTI)
. Abacavir (ABC)
. Lamivudine (3TC)
. Tenofovir (TDF/TAF)
. Zidovudine (AZT)
2. Non-Nucleoside Reverse Transcriptase Inhibitor (NNRTI)
. Efavirenz (EFV)
. Nevirapine (NVP)
. Protease Inhibitors
. Atazanavir (ATV)	
. Lopinavir (LPV)
. Ritonavir (RTV)
. Integrase Inhibitor
. Raltegravir (RAL)
. Dolutegravir (DTG)

"""
    ),
    QuestionItem(
        6,
        title = "6. Antiretroviral Regimen in Pakistan",
        details = """
1. Adults: 
Combination therapy of 2NRTI+ INSTI or PI 
2. Preferred regimen:
Dolutegravir/Lamivudine/Tenofovir DTG/3TC/TDF 
3. Regimen in renal dysfunction: 
Dolutegravir/Lamivudine/Zidovudine DTG/3TC/AZT
4. Regimen in renal dysfunction and anemia: 
Dolutegravir/Lamivudine/Abacavir DTG/3TC/ABC
5. Regimen in DTG psychosis:
Atazanavir/r/Lamivudine/Tenofovir ATVr/3TC/TDF
6. Fixed Drug Combinations (FDC) Available in Pakistan
6.1 Adults FDC (Fix Drug Combination)
•Dolutegravir/Lamivudine/Tenofovir (50mg/300mg/300mg)
•Efavirenz/Lamivudine/Tenofovir (600mg/300mg/300mg)
•Efavirenz/Lamivudine/Zidovudine (600mg/150mg/300mg)
•Nevirapine/Lamivudine/Zidovudine (200mg/150mg/300mg)
•Lamivudine/Tenofovir (300mg/300mg)
6.2 Paeds FDC
•Nevirapine/Lamivudine/Zidovudine (60mg/30mg/50mg)
•Abacavir/Lamivudine dispersible tablets (120mg/60mg)
"""
    ),

    QuestionItem(
        5,
        title = "7. In Which Co-Infections Initiation of ARV is delayed?",
        details = """
1. Tuberculosis
•Non-CNS TB: Start ARV within 2 weeks
•TB meningitis: initiating ART 4-8 weeks after the start of TB treatment

2. Cryptococcal infection
•Start ARV after 8 weeks of cryptococcal treatment
"""
    ),

    QuestionItem(
        6,
        title = "8. Opportunistic Infection Prophylaxis",
        details = """
1. Tuberculosis Preventive Therapy (TPT)
Given when Tuberculin Skin Test (TST) is positive (5-10mm) or in all patients who screen negative for active TB by clinical history.

•Isoniazid 300mg once daily for 6-9 months

2. Co-trimoxazole preventive therapy (CPT)
•It prevents against
•Pneumocystis jirovecii
•Lowers the incidence of toxoplasma and bacterial infections

•Indication
•WHO clinical stage 3 or 4
•CD4 ≤350/mm3

•Discontinuation
•Clinically stable with CD4 ≥350/mm3 and/or
•Virologic suppression after 1 year of ART

•Dose
•Co-trimoxazole 800/160mg once daily
"""
    ),

    QuestionItem(
        7,
        title = "9. Monitoring on ART",
        details = """
1. Call back in 1 to 2 weeks to check for
•Drug Adherence
•Any early side effects
•Address any lingering concerns

2. At 3 months of ART
•Screening questions for TB
•Check viral load. If 50-1000 then recheck in 8 weeks
•Check serum creatinine and urine DR every 3 months during the first year of ART for patients receiving Tenofovir
•Check Hemoglobin if on Zidovudine then monitor 3 monthly

3. At 6 months of ART
•Screening questions for TB
•CD4 count: If CD4 <350 then monitor it 6 monthly till >350
•Viral load: If 50-1000 then recheck in 3 months. If VL <50 then recheck 6 monthly
4. At 12 months of ART
•Screening questions for TB
•Anti-HCV and HepBsAg (if previously negative and unvaccinated)
•STI screening and RPR yearly
"""
    ),

    QuestionItem(
        8,
        title = "10. How to interpret viral load?",
        details = """
•Undetectable: VL <50 copies/ml Recheck VL in 6 months
•Virally Suppressed: VL 50–1000 copies/ml Recheck VL in 3 months
•Virological failure: VL >1000 copies/ml checked twice, 3 months apart with adherence counselling
"""
    ),

    QuestionItem(
        9,
        title = "11. How to manage virological failure?",
        details = """
•Ensure drug compliance
•If on Dolutegravir/Lamivudine/Tenofovir regimen change
•Tenofovir to Zidovudine
•Continue Lamivudine
•Change Dolutegravir to Atazanavir/ritonavir
"""
    ) ,QuestionItem(
        10,
        title = "12.ART Side Effects",
        details = ""
    ),QuestionItem(
        11,
        title = "13.ART Resistance and Treatment Option",
        details = ""
    ),QuestionItem(
        12,
        title = "14.HIV & Opportunistic Infections",
        details = ""
    ),QuestionItem(
        13,
        title = "15.Pre Exposure Prophylaxis (PrEP)",
        details = "Given before exposure to prevent acquisition of HIV\n" +
                "Combination therapy of 2 drugs\n" +
                "•Tenofovir (TDF)+ Lamivudine (3TC)\n" +
                "1. Daily PrEP\n" +
                "•Daily PrEP - 7 days before the exposure is likely to occur to reach protective levels and continued until 28 days after the exposure period ends\n" +
                "2. Event driven PrEP\n" +
                "•MSM ( less frequent exposures)\n" +
                "•2 tablets 2-24 hours before exposure then 1 tablet after exposure and 1 after 24 hours of last tablet"
    ),
        QuestionItem(
        14,
        title = "16. HIV Treatment Centres in Pakistan (Where to refer)",
        details = ""
    )
)

@Composable
fun HIVGuideScreen(viewModel: HivArtCenterViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedItem by remember { mutableStateOf<QuestionItem?>(null) }
    var selectedItem by remember { mutableStateOf(0) }  // renamed for clarity (camelCase)

    val filtered = hivQuestions.filter {
        it.title.contains(searchQuery, true) || it.details.contains(searchQuery, true)
    }
    when (selectedItem) {
        12->{

            OpportunisticInfectionScreen(onBackClick = {
                selectedItem = 0
                expandedItem =null
            },)
        }  11->{

            ArtResistanceTreatmentScreen(onBackClick = {
                selectedItem = 0
                expandedItem =null
            },)
        } 10->{

            ArtSideEffectsScreen(onBackClick = {
                selectedItem = 0
                expandedItem =null
            },)
        }
        14->{

            CenterScreen(viewModel,onBackClick = {
                selectedItem = 0
                expandedItem =null

            },type = "HIV")
        }
        else -> {
            GuideContent(
                questions = filtered,
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                expandedItem = expandedItem,
                onToggle = { item ->
                    selectedItem = item.id
                    expandedItem = if (expandedItem == item) null else item
                }
            )
        }
    }



}