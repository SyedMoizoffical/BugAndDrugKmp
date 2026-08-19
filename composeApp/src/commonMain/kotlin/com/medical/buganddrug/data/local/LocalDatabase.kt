package com.medical.buganddrug.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(
    entities = [
        LovsEntity::class,
        SyndromeIdentificationDataQ2Entity::class,
        SyndromeIdentificationDataEntity::class,
        IvToPOsEntity::class,
        PrecautionFinderListEntity::class,
        CreatinineClearanceEntity::class,
        AntibioticGeneListEntity::class,
        ExposureProPhylaxisListEntity::class,
        BacteriaSusceptibilityListEntity::class,
        HivArtCenterListEntity::class,
        CultureTherapyGuideListEntity::class,
        QSofaScoringPossibilitiesEntity::class,
        News2ScoringPossibilitiesEntity::class
    ],
    version = 2
)
@TypeConverters(DataConverters::class)
@ConstructedBy(LocalDatabaseConstructor::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localDataDao(): LocalDataDao
}

expect object LocalDatabaseConstructor : RoomDatabaseConstructor<LocalDatabase> {
    override fun initialize(): LocalDatabase
}
