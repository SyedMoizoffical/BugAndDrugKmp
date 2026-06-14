package com.medical.buganddrug.ui.HIV

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.search
import com.medical.buganddrug.ui.theme.md_theme_light_shadow
import org.jetbrains.compose.resources.painterResource

// ────────────────────────────────────────────────────────────────────────────────
// Data class
data class STIEntry(
    val syndrome: String,
    val commonAgents: String,
    val signsSymptoms: String,
    val diagnostics: String,
    val differential: String,
    val treatmentPakistan: String,
    val partnerManagement: String,
    val monitoringFollowUp: String
)

// ────────────────────────────────────────────────────────────────────────────────
// Searchable Screen with ALL 7 syndromes
@Composable
fun STIManagementScreen(onBackClick: () -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }

    val allEntries = listOf(
        // 1. Urethral discharge (Urethritis)
        STIEntry(
            syndrome = "1. Urethral discharge (Urethritis)",
            commonAgents = "Neisseria gonorrhoeae (GC), Chlamydia trachomatis (CT), Mycoplasma genitalium, Trichomonas vaginalis",
            signsSymptoms = """Ask about purulent/mucoid discharge, dysuria, urethral itching.
• Purulent, thick discharge → Gonorrhea more likely
• Mucoid or scant discharge → Chlamydia
• Persistent symptoms after standard treatment → Mycoplasma genitalium
• Subacute symptoms with partner having vaginal discahrge and itching→ Trichomonas vaginalis""",
            diagnostics = """• Copious yellow/green discharge → Gonorrhea
• Mild clear discharge → Chlamydia
Perform tests where available; otherwise treat empirically based on the presenting syndrome at the first visit. Do not delay treatment while awaiting results.
Screen for HIV and syphilis in all cases. First Void Urine (FVU) NAAT or urethral swab NAAT for Gonococcus (GC) /Chlamydia Trachomatis (CT), Urethral swab for Gram stain & culture.""",
            differential = "UTI, prostatitis, balanitis",
            treatmentPakistan = """Treat for GC and CT at the first visit
*Ceftriaxone 500 mg IM single dose (GC)
* Plus doxycycline 100 mg orally twice daily for 7 days (CT)
* Alternative (if doxycycline contraindicated): Azithromycin 2 g orally as a single dose
If no response to the above regimen
* Moxifloxacin 400 mg orally once daily for 14 days (to cover Mycoplasma genitalium)
Trichomonas vaginalis
* Metronidazole 500 mg orally twice daily for 7 days""",
            partnerManagement = "Treat partners from last 60 days with Inj Ceftriaxone 500mg IM single dose (GC) +- Azithromycin 1gm single dose or doxyxcyline 100mg BD for 7 days for CT; abstain from sex for 7 days post treatment and until asymptomatic",
            monitoringFollowUp = "Symptom review at 7 days. Test of cure is routinely not recommended unless symptoms persist or treatment fails at 1 week for gonorrhea and at 4 weeks for chlamydia."
        ),

        // 2. Vaginal discharge (Vaginitis)
        STIEntry(
            syndrome = "2. Vaginal discharge (Vaginitis)",
            commonAgents = "Bacterial Vaginosis (BV), Trichomonas vaginalis (TV), Candida, GC/CT (Neisseria gonorrhoeae, Chlamydia trachomatis)",
            signsSymptoms = """• Fishy odor, thin gray discharge → Bacterial vaginosis
• Frothy yellow-green discharge, itching → Trichomonas
• Thick white curdy discharge, intense itching → Candida
• Post-coital bleeding, pelvic pain → Cervicitis (GC/CT)""",
            diagnostics = """• Homogeneous gray discharge coating vagina → BV
• Strawberry cervix → Trichomonas
• Adherent white plaques → Candida
• Mucopurulent cervical discharge → GC/CT
Perform tests where available; otherwise treat empirically based on the presenting syndrome at the first visit. Do not delay treatment while awaiting results.
Screen for HIV and syphilis in all cases. Wet mount microscopy for clue cells in BV and motile trichomonads in Trichomonas, vaginal pH > 4.5 in BV & TV, first void urine or vaginal swab NAAT GC/CT (Neisseria gonorrhoeae, Chlamydia trachomatis) and trichomonas vaginalis""",
            differential = "Physiological discharge, cervicitis",
            treatmentPakistan = """Bacterial Vaginosis/Trichomonas vaginalis: Metronidazole 500 mg orally twice daily for 7 days
* ± Fluconazole 150 mg orally as a single dose if Candida is suspected
Add GC/CT coverage (Neisseria gonorrhoeae, Chlamydia trachomatis) if cervicitis is present Ceftriaxone 500 mg IM single dose
* Plus doxycycline 100 mg orally twice daily for 7 days. If doxycycline is contraindicated: Azithromycin 2 g orally as a single dose""",
            partnerManagement = """Treat all partners from the last 60 days.
Trichomonas vaginalis: Metronidazole 500 mg PO BD for 7 days.
Gonorrhea ± Chlamydia: Ceftriaxone 500 mg IM single dose ± Azithromycin 1 g single dose or Doxycycline 100 mg BD for 7 days.
Abstain from sexual activity for 7 days after treatment and until asymptomatic.""",
            monitoringFollowUp = "Reassess for symptoms at 7–14 days"
        ),

        // 3. Painful genital ulcer
        STIEntry(
            syndrome = "3. Painful genital ulcer",
            commonAgents = "Herpes Simplex Virus (HSV), Haemophilus ducreyi (Chancroid)",
            signsSymptoms = """• Painful ulcers, recurrent → HSV
• Painful ulcer + tender nodes → Chancroid""",
            diagnostics = """• Multiple shallow ulcers → HSV
• Ragged painful ulcer with purulent base → Chancroid
Perform tests where available; otherwise treat empirically based on the presenting syndrome at the first visit. Do not delay treatment while awaiting results.
Screen for HIV and syphilis in all cases. HSV PCR from ulcer, hemophilus culture if available""",
            differential = "Traumatic ulcers, Behçet disease",
            treatmentPakistan = """HSV
* Acyclovir 400 mg orally three times daily for 7–10 days
* or valacyclovir 1 g orally twice daily for 7–10 days
If chancroid is suspected
* Add azithromycin 1 g orally as a single dose or ceftriaxone 250 mg IM as a single dose""",
            partnerManagement = """* Partner evaluation and counseling
* Abstain from sexual activity in the presence of active lesions
HSV
* Treat only the symptomatic partner
Chancroid
* Treat all sexual partners from the preceding 10 days before the patient’s symptom onset, even if asymptomatic
* Azithromycin 1 g orally as a single dose or ceftriaxone 250 mg IM as a single dose single dose.""",
            monitoringFollowUp = "Clinical improvement in 7–10 days"
        ),

        // 4. Painless genital ulcer
        STIEntry(
            syndrome = "4. Painless genital ulcer",
            commonAgents = "Treponema pallidum (Syphilis), Klebsiella granulomatis (Donovanosis /Granuloma inguinale), Chlamydia trachomatis (Lymphogranuloma Venereum LGV)",
            signsSymptoms = """• Single painless indurated ulcer, firm base → Syphilis
• Beefy red, serpiginous painless ulcer, bleeds easily → Granuloma inguinale
• Tender lymphadenopathy→ LGV""",
            diagnostics = """• Clean indurated ulcer → Syphilis
• Granulating red lesion → Granuloma inguinale
• Tender lymphadenopathy→ LGV
Perform tests where available; otherwise treat empirically based on the presenting syndrome at the first visit. Do not delay treatment while awaiting results.
Syphilis: Serum non treponemal test (RPR/VDRL) + treponemal test (TPHA), HIV""",
            differential = "Carcinoma, trauma",
            treatmentPakistan = """Early syphilis (primary, secondary, early latent)
* Benzathine penicillin G 2.4 million units IM single dose (1.2 million units in each buttock)
Late latent syphilis
* Benzathine penicillin G 2.4 million units IM once weekly for 3 weeks (1.2 million units in each buttock per dose)
Granuloma inguinale
* Azithromycin 1 g orally once weekly for at least 3 weeks or until lesions resolve
* Alternative regimens:
  * Doxycycline 100 mg orally twice daily for 3 weeks
  * TMP–SMX DS orally every 12 hours for 3 weeks
  * Ciprofloxacin 750 mg orally twice daily for 3 weeks
Lymphogranuloma venereum (LGV)
* Doxycycline 100 mg orally twice daily for 21 days
* Alternative regimens:
  * Erythromycin 500 mg orally four times daily for 21 days
  * Azithromycin 1 g orally once weekly for 3 weeks
* Aspiration of buboes if present""",
            partnerManagement = """Syphilis
* Treat sexual partners within the preceding 90 days
* Benzathine penicillin G 2.4 million units IM single dose
* (1.2 million units in each buttock)
Granuloma inguinale
* Treat sexual partners within the preceding 60 days
* Same regimen as patient treatment
Lymphogranuloma venereum (LGV)
* Treat sexual partners within the preceding 60 days
* Azithromycin 1 g orally as a single dose or doxycycline 100 mg orally twice daily for 7 days""",
            monitoringFollowUp = "Check VDRL titres at 6 & 12 months"
        ),

        // 5. Painful inguinal nodes
        STIEntry(
            syndrome = "5. Painful inguinal nodes",
            commonAgents = "Chlamydia trachomatis(Lymphogranuloma Venereum LGV) , Haemophilus ducreyi (Chancroid)",
            signsSymptoms = """• Large tender nodes ± systemic symptoms → LGV
• Painful unilateral node + genital ulcer → Chancroid
• Firm nodes, minimal pain → Syphilis
• Fluctuant painful nodes → Chancroid or LGV
• Bilateral rubbery nodes → Syphilis""",
            diagnostics = """Perform tests where available; otherwise treat empirically based on the presenting syndrome at the first visit. Do not delay treatment while awaiting results.
Screen for HIV and syphilis in all cases. First void urine or urethral/vaginal swabs for CT (Chlamydia trachomatis).""",
            differential = "TB, skin infection",
            treatmentPakistan = """Lymphogranuloma venereum (LGV)
* Doxycycline 100 mg orally twice daily for 21 days
* Alternative regimens:
  * Erythromycin 500 mg orally four times daily for 21 days
  * Azithromycin 1 g orally once weekly for 3 weeks
* Aspiration of buboes if present
Chancroid
* Azithromycin 1 g orally as a single dose or ceftriaxone 250 mg IM as a single dose""",
            partnerManagement = """Lymphogranuloma venereum (LGV)
* Treat sexual partners within the preceding 60 days
* Azithromycin 1 g orally as a single dose or doxycycline 100 mg orally twice daily for 7 days

Chancroid
* Treat all sexual partners from the preceding 10 days before the patient’s symptom onset, even if asymptomatic
* Azithromycin 1 g orally as a single dose or ceftriaxone 250 mg IM as a single dose""",
            monitoringFollowUp = "LGV: Review at 3 weeks"
        ),

        // 6. Lower abdominal pain (Pelvic Inflammatory Disease)
        STIEntry(
            syndrome = "6. Lower abdominal pain (Pelvic Inflammatory Disease)",
            commonAgents = "Neisseria gonorrhoeae (GC), Chlamydia trachomatis (CT), anaerobes",
            signsSymptoms = """• Bilateral pelvic pain, fever
• Recent STI or unsafe sex
• Irregular vaginal bleeding""",
            diagnostics = """• Cervical motion tenderness
• Adnexal tenderness
Treat PID immediately; do not delay for tests. Pregnancy test, NAAT for GC (Neisseria gonorrhoeae)/CT (Chlamydia trachomatis), Ultrasound pelvis""",
            differential = "Ectopic pregnancy, appendicitis",
            treatmentPakistan = "Ceftriaxone 500 mg IM single dose + Doxycycline 100 mg orally twice daily for 14 days + Metronidazole 500 mg orally twice daily for 14 days",
            partnerManagement = "Treat partners from last 60 days for Chalymdia/ Gonorrhea with Ceftriaxone 500 mg IM single dose ± Azithromycin 1 g single dose or Doxycycline 100 mg BD for 7 days.",
            monitoringFollowUp = "Review 48–72 hours"
        ),

        // 7. Anorectal pain/discharge (Proctitis)
        STIEntry(
            syndrome = "7. Anorectal pain/discharge (Proctitis)",
            commonAgents = "Neisseria gonorrhoeae (GC), Chlamydia trachomatis CT (LGV Lymphogranuloma Venereum ), Herpes Simplex Virus (HSV), Treponema pallidum (Syphilis)",
            signsSymptoms = """• Severe rectal pain, tenesmus, bleeding → LGV
• Rectal discharge after receptive anal sex → GC/CT
• Painful ulcers, burning → HSV""",
            diagnostics = """• Friable rectal mucosa, bleeding → LGV
• Purulent discharge → GC
• Ulcers on perianal skin → HSV
Perform tests where available; otherwise treat empirically based on the presenting syndrome at the first visit. Do not delay treatment while awaiting results.
Screen for HIV and syphilis in all cases. Rectal NAAT for GC (Neisseria gonorrhoeae), CT (Chlamydia trachomatis), HSV PCR, HIV serology, syphilis testing (VDRL/TPHA)""",
            differential = "Inflammatory Bowel Disease, hemorrhoids",
            treatmentPakistan = "Ceftriaxone 500 mg IM single dose + Doxycycline 100 mg orally twice daily for 7 days (21 days if LGV suspected)",
            partnerManagement = """Treat all partners from the last 60 days.
Gonorrhea ± Chlamydia: Ceftriaxone 500 mg IM single dose ± Azithromycin 1 g single dose or Doxycycline 100 mg BD for 7 days.
Abstain from sexual activity for 7 days after treatment and until asymptomatic.""",
            monitoringFollowUp = "Repeat testing 3 months"
        )
    )
    // Filter entries based on search query (case-insensitive)
    val filteredEntries by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                allEntries
            } else {
                val lowerQuery = searchQuery.lowercase()
                allEntries.filter { entry ->
                    entry.syndrome.lowercase().contains(lowerQuery) ||
                            entry.commonAgents.lowercase().contains(lowerQuery) ||
                            entry.signsSymptoms.lowercase().contains(lowerQuery) ||
                            entry.diagnostics.lowercase().contains(lowerQuery) ||
                            entry.differential.lowercase().contains(lowerQuery) ||
                            entry.treatmentPakistan.lowercase().contains(lowerQuery) ||
                            entry.partnerManagement.lowercase().contains(lowerQuery) ||
                            entry.monitoringFollowUp.lowercase().contains(lowerQuery)
                }
            }
        }
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.search),
                        contentDescription = null,
                        tint = Color(0xFF800080),
                        modifier = Modifier.size(24.dp) // actual icon size

                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            // Search bar
            Spacer(modifier = Modifier.height(12.dp))


            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredEntries.isEmpty()) {
                    item {
                        Text(
                            text = "No matching syndromes found.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(filteredEntries) { entry ->
                        STICard(entry)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Guidelines based on WHO 2021 & NACP Pakistan. Always verify with latest updates (as of 2025).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

}

// ────────────────────────────────────────────────────────────────────────────────
// STICard & DetailRow (same as before)
@Composable
private fun STICard(entry: STIEntry) {
    var expanded by remember { mutableStateOf(false) }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val tonalColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { expanded = !expanded  },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    )  {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entry.syndrome,
                    modifier = Modifier.weight(1f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = md_theme_light_shadow
                    )                )

                Icon(
                     painter = painterResource(Res.drawable.arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(if (expanded) 180f else 0f),
                    tint = Color(0xFF800080)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(tonalColor)
                        .padding(12.dp)
                ) {
                    DetailRow("Common Agents", entry.commonAgents)
                    Divider()
                    DetailRow("Signs & Symptoms", entry.signsSymptoms)
                    Divider()
                    DetailRow("Diagnostics", entry.diagnostics)
                    Divider()
                    DetailRow("Differential Diagnosis", entry.differential)
                    Divider()
                    DetailRow("Treatment (Pakistan)", entry.treatmentPakistan)
                    Divider()
                    DetailRow("Partner Management", entry.partnerManagement)
                    Divider()
                    DetailRow("Monitoring & Follow-up", entry.monitoringFollowUp)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
    }
}