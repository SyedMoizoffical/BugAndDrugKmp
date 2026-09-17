package com.medical.buganddrug.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLovs(lovs: LovsEntity): Long

    @Query("SELECT * FROM lovs WHERE id = 1")
    suspend fun getLovs(): LovsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyndromeIdentificationDataQ2(data: SyndromeIdentificationDataQ2Entity): Long

    @Query("SELECT * FROM syndrome_identification_data_q2 WHERE id = 1")
    suspend fun getSyndromeIdentificationDataQ2(): SyndromeIdentificationDataQ2Entity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyndromeIdentificationData(data: SyndromeIdentificationDataEntity): Long

    @Query("SELECT * FROM syndrome_identification_data WHERE id = 1")
    suspend fun getSyndromeIdentificationData(): SyndromeIdentificationDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIvToPOs(data: IvToPOsEntity): Long

    @Query("SELECT * FROM iv_to_pos WHERE id = 1")
    suspend fun getIvToPOs(): IvToPOsEntity?
//done
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrecautionFinderList(data: PrecautionFinderListEntity): Long
//done
    @Query("SELECT * FROM precaution_finder_list WHERE id = 1")
    suspend fun getPrecautionFinderList(): PrecautionFinderListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreatinineClearance(data: CreatinineClearanceEntity): Long

    @Query("SELECT * FROM creatinine_clearance WHERE id = 1")
    suspend fun getCreatinineClearance(): CreatinineClearanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAntibioticGeneList(data: AntibioticGeneListEntity): Long

    @Query("SELECT * FROM antibiotic_gene_list WHERE id = 1")
    suspend fun getAntibioticGeneList(): AntibioticGeneListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExposureProPhylaxisList(data: ExposureProPhylaxisListEntity): Long

    @Query("SELECT * FROM exposure_prophylaxis_list WHERE id = 1")
    suspend fun getExposureProPhylaxisList(): ExposureProPhylaxisListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBacteriaSusceptibilityList(data: BacteriaSusceptibilityListEntity): Long

    @Query("SELECT * FROM bacteria_susceptibility_list WHERE id = 1")
    suspend fun getBacteriaSusceptibilityList(): BacteriaSusceptibilityListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHivArtCenterList(data: HivArtCenterListEntity): Long

    @Query("SELECT * FROM hiv_art_center_list WHERE id = 1")
    suspend fun getHivArtCenterList(): HivArtCenterListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCultureTherapyGuideList(data: CultureTherapyGuideListEntity): Long

    @Query("SELECT * FROM culture_therapy_guide_list WHERE id = 1")
    suspend fun getCultureTherapyGuideList(): CultureTherapyGuideListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQSofaScoringPossibilities(data: QSofaScoringPossibilitiesEntity): Long

    @Query("SELECT * FROM qsofa_scoring_possibilities WHERE id = 1")
    suspend fun getQSofaScoringPossibilities(): QSofaScoringPossibilitiesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews2ScoringPossibilities(data: News2ScoringPossibilitiesEntity): Long

    @Query("SELECT * FROM news2_scoring_possibilities WHERE id = 1")
    suspend fun getNews2ScoringPossibilities(): News2ScoringPossibilitiesEntity?

    @Query("DELETE FROM lovs")
    suspend fun clearLovs(): Int

    @Query("DELETE FROM syndrome_identification_data_q2")
    suspend fun clearSyndromeIdentificationDataQ2(): Int

    @Query("DELETE FROM syndrome_identification_data")
    suspend fun clearSyndromeIdentificationData(): Int

    @Query("DELETE FROM iv_to_pos")
    suspend fun clearIvToPOs(): Int

    @Query("DELETE FROM precaution_finder_list")
    suspend fun clearPrecautionFinderList(): Int

    @Query("DELETE FROM creatinine_clearance")
    suspend fun clearCreatinineClearance(): Int

    @Query("DELETE FROM antibiotic_gene_list")
    suspend fun clearAntibioticGeneList(): Int

    @Query("DELETE FROM exposure_prophylaxis_list")
    suspend fun clearExposureProPhylaxisList(): Int

    @Query("DELETE FROM bacteria_susceptibility_list")
    suspend fun clearBacteriaSusceptibilityList(): Int

    @Query("DELETE FROM hiv_art_center_list")
    suspend fun clearHivArtCenterList(): Int

    @Query("DELETE FROM culture_therapy_guide_list")
    suspend fun clearCultureTherapyGuideList(): Int

    @Query("DELETE FROM qsofa_scoring_possibilities")
    suspend fun clearQSofaScoringPossibilities(): Int

    @Query("DELETE FROM news2_scoring_possibilities")
    suspend fun clearNews2ScoringPossibilities(): Int
}
