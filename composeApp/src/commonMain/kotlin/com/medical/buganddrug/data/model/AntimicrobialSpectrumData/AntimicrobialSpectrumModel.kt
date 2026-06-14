package com.medical.buganddrug.data.model.AntimicrobialSpectrumData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class AntimicrobialSpectrumModel(
    val gridLists: List<BacteriaRow>
)
@Serializable
data class BacteriaRow(
    @SerialName("id")
    val id: Int,

    @SerialName("Label")
    val label: String,

    @SerialName("Penicillin G")
    val penicillinG: Int?,

    @SerialName("Cloxacillin")
    val cloxacillin: Int?,

    @SerialName("Ampicillin")
    val ampicillin: Int?,

    @SerialName("Amoxicillin")
    val amoxicillin: Int?,

    @SerialName("Amoxicillin-clavulanate")
    val amoxicillinClavulanate: Int?,

    @SerialName("Piperacillin-tazobactam")
    val piperacillinTazobactam: Int?,

    @SerialName("Cephalexin (1st generation)")
    val cephalexin: Int?,

    @SerialName("Cefazolin (1st generation)")
    val cefazolin: Int?,

    @SerialName("Cefixime (2nd generation)")
    val cefixime: Int?,

    @SerialName("Cefuroxime (2nd generation)")
    val cefuroxime: Int?,

    @SerialName("Cefpodoxime (3rd generation)")
    val cefpodoxime: Int?,

    @SerialName("Cefotaxime (3rd generation)")
    val cefotaxime: Int?,

    @SerialName("Cefoperazone (3rd generation)")
    val cefoperazone: Int?,

    @SerialName("Ceftriaxone (3rd generation)")
    val ceftriaxone: Int?,

    @SerialName("Ceftazidime (3rd generation)")
    val ceftazidime: Int?,

    @SerialName("Cefepime (4th generation)")
    val cefepime: Int?,

    @SerialName("Ceftaroline (5th generation)")
    val ceftaroline: Int?,

    @SerialName("Cefiderocol (5th generation)")
    val cefiderocol: Int?,

    @SerialName("Cefoperazone sulbactam")
    val cefoperazoneSulbactam: Int?,

    @SerialName("Ceftazidime-Avibactam")
    val ceftazidimeAvibactam: Int?,

    @SerialName("Ertapenem")
    val ertapenem: Int?,

    @SerialName("Imipenem")
    val imipenem: Int?,

    @SerialName("Meropenem")
    val meropenem: Int?,

    @SerialName("Aztreonam")
    val aztreonam: Int?,

    @SerialName("Aztreonam-avibactam")
    val aztreonamAvibactam: Int?,

    @SerialName("Amikacin")
    val amikacin: Int?,

    @SerialName("Gentamicin")
    val gentamicin: Int?,

    @SerialName("Ciprofloxacin")
    val ciprofloxacin: Int?,

    @SerialName("Levofloxacin")
    val levofloxacin: Int?,

    @SerialName("Moxifloxacin")
    val moxifloxacin: Int?,

    @SerialName("TMP-SMX")
    val tmpSmx: Int?,

    @SerialName("Vancomycin")
    val vancomycin: Int?,

    @SerialName("Daptomycin")
    val daptomycin: Int?,

    @SerialName("Linezolid")
    val linezolid: Int?,

    @SerialName("Clindamycin")
    val clindamycin: Int?,

    @SerialName("Doxycycline")
    val doxycycline: Int?,

    @SerialName("Minocycline")
    val minocycline: Int?,

    @SerialName("Tigecycline")
    val tigecycline: Int?,

    @SerialName("Colistin")
    val colistin: Int?,

    @SerialName("Erythromycin")
    val erythromycin: Int?,

    @SerialName("Azithromycin")
    val azithromycin: Int?,

    @SerialName("Clarithromycin")
    val clarithromycin: Int?,

    @SerialName("Metronidazole")
    val metronidazole: Int?,

    @SerialName("Nitrofurantoin")
    val nitrofurantoin: Int?,

    @SerialName("Fosfomycin (IV)")
    val fosfomycinIV: Int?,

    @SerialName("Fosfomycin (PO)")
    val fosfomycinPO: Int?
)
