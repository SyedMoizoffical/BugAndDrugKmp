package com.medical.buganddrug.ui.VaccineIndication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionFourViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q4.SingleSelectSearchableSpinnerDialog
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import org.jetbrains.compose.resources.painterResource

// ----------------------------------------------------------------------
// MOCK MODEL
// ----------------------------------------------------------------------
data class VaccineIndication(
    val indication: String,
    val vaccine: String,
    val type: String,
    val schedule: String,
    val ageGroup: String,
    val route: String,
    val contraindications: String,
    val sideEffects: String,
    val notes: String,
    val catchUp: String,
    val pregnancy: String,
    val immunocompromised: String,
    val travelRecommendation: String,
    val aspleniaRecommendation: String
)

// ----------------------------------------------------------------------
// MOCK DATA LIST
// ----------------------------------------------------------------------
val mockVaccineList = listOf(

    VaccineIndication(
        indication = "Tuberculosis",
        vaccine = "BCG",
        type = "Live attenuated",
        schedule = "1 dose at birth",
        ageGroup = "Newborn",
        route = "Intradermal (left upper arm)",
        contraindications = "Severe immunodeficiency; severe illness",
        sideEffects = "Local ulceration, lymphadenitis, mild fever",
        notes = "Given at birth; may leave scar.",
        catchUp = "Give up to 1 month of age; later require Mantoux test",
        pregnancy = "Contraindicated",
        immunocompromised = "Contraindicated in severe immunocompromise",
        travelRecommendation = "No",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Poliomyelitis",
        vaccine = "OPV & IPV",
        type = "OPV: Live attenuated; IPV: Inactivated",
        schedule = "Birth OPV → 6,10,14 weeks → IPV booster at 9 months",
        ageGroup = "Infant/Children/Adult",
        route = "OPV oral, IPV intramuscular",
        contraindications = "Severe allergy to components",
        sideEffects = "OPV: Mild fever; rare VAPP. IPV: Pain at site",
        notes = "OPV boosts gut immunity; IPV boosts systemic immunity",
        catchUp = "Extra OPV doses in SIA campaigns",
        pregnancy = "Avoid OPV; use IPV if required",
        immunocompromised = "Avoid OPV; use IPV",
        travelRecommendation = "Yes, booster required when leaving Pakistan",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Diphtheria",
        vaccine = "Pentavalent, DTaP, Tdap",
        type = "Toxoid + recombinant + inactivated",
        schedule = "6,10,14 weeks; boosters at 15–18 months & 4–6 years",
        ageGroup = "Infant/Child/Adult",
        route = "Intramuscular",
        contraindications = "Anaphylaxis, encephalopathy, GBS history",
        sideEffects = "Pain, redness, swelling, fever",
        notes = "Reduces number of injections by combining vaccines",
        catchUp = "Continue from where interrupted",
        pregnancy = "Tdap each pregnancy (27–36 weeks)",
        immunocompromised = "Safe",
        travelRecommendation = "No",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Pneumococcal Pneumonia",
        vaccine = "PCV & PPSV23",
        type = "PCV: Conjugate; PPSV: Polysaccharide",
        schedule = "6, 10, 14 weeks + 15 months; adults PCV then PPSV",
        ageGroup = "Infants & Adults ≥50 or high-risk",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Fever, pain at injection site",
        notes = "PCV is given before PPSV",
        catchUp = "Do not restart; count received doses",
        pregnancy = "Safe if indicated",
        immunocompromised = "Yes (important)",
        travelRecommendation = "Yes for high-risk areas",
        aspleniaRecommendation = "Yes (before splenectomy)"
    ),

    VaccineIndication(
        indication = "Rotavirus",
        vaccine = "Rotavirus",
        type = "Live attenuated",
        schedule = "6 and 10 weeks",
        ageGroup = "Infant",
        route = "Oral",
        contraindications = "SCID; history of intussusception",
        sideEffects = "Mild vomiting, diarrhea",
        notes = "First dose at least 2 weeks before OPV",
        catchUp = "Give missed dose ASAP",
        pregnancy = "Contraindicated",
        immunocompromised = "Contraindicated",
        travelRecommendation = "No",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Measles, Mumps, Rubella",
        vaccine = "MMR",
        type = "Live attenuated",
        schedule = "9 months, 15 months",
        ageGroup = "Infant/Child",
        route = "SC",
        contraindications = "Pregnancy; severe immunosuppression",
        sideEffects = "Fever, rash, injection site reaction",
        notes = "2 doses improve immunity",
        catchUp = "2 doses 4 weeks apart",
        pregnancy = "Contraindicated",
        immunocompromised = "Contraindicated",
        travelRecommendation = "Yes for outbreaks/measles areas",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Typhoid",
        vaccine = "TCV",
        type = "Conjugate",
        schedule = "1 dose at 9 months",
        ageGroup = "Infant/Child",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Pain, fever",
        notes = "Strong protection for 4+ years",
        catchUp = "Single dose",
        pregnancy = "Use only if benefit outweighs risk",
        immunocompromised = "Generally safe",
        travelRecommendation = "Yes for endemic areas",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Tetanus",
        vaccine = "DTaP, Tdap, Td",
        type = "Toxoid (inactivated)",
        schedule = "DTaP at 2,4,6,15–18 months & 4–6 years; Tdap every 10 years",
        ageGroup = "Children & Adults",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Pain, fever",
        notes = "Tdap recommended in every pregnancy",
        catchUp = "As per maternal history",
        pregnancy = "Tdap mandatory",
        immunocompromised = "Safe",
        travelRecommendation = "No",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Influenza",
        vaccine = "Influenza (IIV/RIV)",
        type = "Inactivated or recombinant",
        schedule = "Annual; 2 doses first time (6m–8y)",
        ageGroup = "≥6 months",
        route = "IM",
        contraindications = "Severe allergy; egg allergy caution",
        sideEffects = "Soreness, fever, myalgia",
        notes = "Annual vaccination recommended",
        catchUp = "Annual dosing",
        pregnancy = "Safe",
        immunocompromised = "Use inactivated only",
        travelRecommendation = "Yes (seasonal)",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Chickenpox",
        vaccine = "Varicella",
        type = "Live attenuated",
        schedule = "12–18 months + 4–6 years",
        ageGroup = "Children/Adults",
        route = "SC",
        contraindications = "Pregnancy, severe immunosuppression",
        sideEffects = "Fever, local reactions",
        notes = "Check serostatus in adults",
        catchUp = "2-dose series",
        pregnancy = "Contraindicated",
        immunocompromised = "Contraindicated",
        travelRecommendation = "Yes if non-immune",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Shingles",
        vaccine = "RZV",
        type = "Recombinant subunit",
        schedule = "2 doses, 2–6 months apart",
        ageGroup = "≥50 years",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Pain, fatigue, fever",
        notes = "Preferred over live vaccine",
        catchUp = "Complete 2-dose series",
        pregnancy = "Use only if needed",
        immunocompromised = "Recommended",
        travelRecommendation = "No",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Hepatitis A",
        vaccine = "Hepatitis A",
        type = "Inactivated",
        schedule = "2 doses (0 and 6–12 months)",
        ageGroup = "Children & Adults",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Pain, fever",
        notes = "Recommended for travelers",
        catchUp = "Complete 2 doses",
        pregnancy = "Safe",
        immunocompromised = "Generally safe",
        travelRecommendation = "Yes",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Hepatitis B",
        vaccine = "HepB",
        type = "Recombinant",
        schedule = "Birth dose + 6,10,14 weeks (paeds) / 0,1,6 months (adult)",
        ageGroup = "Infant/Adult",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Pain, fatigue, fever",
        notes = "High-risk groups need vaccination",
        catchUp = "Complete all doses",
        pregnancy = "Safe",
        immunocompromised = "Recommended",
        travelRecommendation = "Yes",
        aspleniaRecommendation = "No"
    ),

    VaccineIndication(
        indication = "Meningitis/Septicaemia",
        vaccine = "MenACWY / MenB",
        type = "Conjugate / Recombinant",
        schedule = "1–2 doses depending on age & risk",
        ageGroup = "Infants → Adults",
        route = "IM",
        contraindications = "Severe allergy",
        sideEffects = "Pain, fever",
        notes = "High-risk groups include asplenia & complement deficiency",
        catchUp = "Based on risk",
        pregnancy = "Use if indicated",
        immunocompromised = "Recommended",
        travelRecommendation = "Yes for meningitis belt",
        aspleniaRecommendation = "Yes"
    )
)


// ----------------------------------------------------------------------
// MAIN SCREEN
// ----------------------------------------------------------------------

@Composable
fun VaccineIndicationScreen(
    viewModel: QuestionFourViewModel,
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
)
{

    val scrollState = rememberScrollState()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            topBar(
                topic = "Vaccine Indication Finder",
                patientType = "Vaccine Indication Finder",
                onBackClick = onBackClick
            )

        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                      Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF3E5F5)   // Very soft lavender
                        )
                    )
                )
                .padding(innerPadding)
        )  {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {

                Text(
                    text = "Select a Indication",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // CONDITIONS LIST FOR DROPDOWN
                val conditions = mockVaccineList.mapIndexed { index, item ->
                    item.indication to index
                }

                SingleSelectSearchableSpinnerDialog(
                    label = "Indication",
                    items = conditions,
                    itemLabel = { it.first },
                    selectedItem = selectedIndex,
                    onItemSelected = { selectedIndex = it?.second }
                )

                Spacer(modifier = Modifier.height(20.dp))

                val selected = selectedIndex?.let { mockVaccineList[it] }

                if (selected != null) {
                    VaccineDetailCard(selected)
                } else {
                    EmptyCardMessage()
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = selectedIndex != null,
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

// ----------------------------------------------------------------------
// DETAILS CARD
// ----------------------------------------------------------------------
@Composable
fun VaccineDetailCard(data: VaccineIndication) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // ---- HEADER BAR (Matches Q4 UI) ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEDCFF))
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Vaccine Details: ${data.indication}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---- MATCHED INFO ROWS ----
            InfoRow("Vaccine", data.vaccine)
            InfoRow("Type", data.type)
            InfoRow("Schedule", data.schedule)
            InfoRow("Age Group", data.ageGroup)
            InfoRow("Route", data.route)
            InfoRow("Contraindications", data.contraindications)
            InfoRow("Side Effects", data.sideEffects)
            InfoRow("Notes", data.notes)
            InfoRow("Catch-up", data.catchUp)
            InfoRow("Pregnancy", data.pregnancy)
            InfoRow("Immunocompromised", data.immunocompromised)
            InfoRow("Travel Recommendation", data.travelRecommendation)
            InfoRow("Asplenia Recommendation", data.aspleniaRecommendation)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(value, color = MaterialTheme.colorScheme.onSurface)
        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}



// ----------------------------------------------------------------------
// EMPTY MESSAGE WHEN NOTHING SELECTED
// ----------------------------------------------------------------------
@Composable
fun EmptyCardMessage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.info),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Please select an indication to view vaccine details",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }
    }
}


