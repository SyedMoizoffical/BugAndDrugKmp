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

    @SerialName("Penicilling")
    val penicillinG: Int?,

    @SerialName("Cloxacillin")
    val cloxacillin: Int?,

    @SerialName("Ampicillin")
    val ampicillin: Int?,

    @SerialName("Amoxicillin")
    val amoxicillin: Int?,

    @SerialName("Amoxicillinclavulanate")
    val amoxicillinClavulanate: Int?,

    @SerialName("Piperacillintazobactam")
    val piperacillinTazobactam: Int?,

    @SerialName("Cephalexin1stgeneration")
    val cephalexin: Int?,

    @SerialName("Cefazolin1stgeneration")
    val cefazolin: Int?,

    @SerialName("Cefixime2ndgeneration")
    val cefixime: Int?,

    @SerialName("Cefuroxime2ndgeneration")
    val cefuroxime: Int?,

    @SerialName("Cefpodoxime3rdgeneration")
    val cefpodoxime: Int?,

    @SerialName("Cefotaxime3rdgeneration")
    val cefotaxime: Int?,

    @SerialName("Cefoperazone3rdgeneration")
    val cefoperazone: Int?,

    @SerialName("Ceftriaxone3rdgeneration")
    val ceftriaxone: Int?,

    @SerialName("Ceftazidime3rdgeneration")
    val ceftazidime: Int?,

    @SerialName("Cefepime4thgeneration")
    val cefepime: Int?,

    @SerialName("Ceftaroline5thgeneration")
    val ceftaroline: Int?,

    @SerialName("Cefiderocol5thgeneration")
    val cefiderocol: Int?,

    @SerialName("Cefoperazonesulbactam")
    val cefoperazoneSulbactam: Int?,

    @SerialName("CeftazidimeAvibactam")
    val ceftazidimeAvibactam: Int?,

    @SerialName("Ertapenem")
    val ertapenem: Int?,

    @SerialName("Imipenem")
    val imipenem: Int?,

    @SerialName("Meropenem")
    val meropenem: Int?,

    @SerialName("Aztreonam")
    val aztreonam: Int?,

    @SerialName("Aztreonamavibactam")
    val aztreonamAvibactam: Int?,

    @SerialName("Amikacin")
    val amikacin: Int?,

    @SerialName("gentamicin")
    val gentamicin: Int?,

    @SerialName("Ciprofloxacin")
    val ciprofloxacin: Int?,

    @SerialName("Levofloxacin")
    val levofloxacin: Int?,

    @SerialName("Moxifloxacin")
    val moxifloxacin: Int?,

    @SerialName("TMPSMX")
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

    @SerialName("FosfomycinIV")
    val fosfomycinIV: Int?,

    @SerialName("FosfomycinPO")
    val fosfomycinPO: Int?
)
