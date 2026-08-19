package com.medical.buganddrug.AppModule

import com.medical.buganddrug.data.local.LocalDataDao
import com.medical.buganddrug.data.local.LocalDatabase
import com.medical.buganddrug.data.local.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<LocalDatabase> {
        val builder = getDatabaseBuilder(null)
        builder.fallbackToDestructiveMigration(true)
        builder.build()
    }
    single<LocalDataDao> {
        get<LocalDatabase>().localDataDao()
    }
}
