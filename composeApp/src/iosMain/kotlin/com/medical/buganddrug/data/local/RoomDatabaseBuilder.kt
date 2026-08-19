package com.medical.buganddrug.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<LocalDatabase> {

    val documentsDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    ) ?: error("Unable to access Documents directory")

    val dbPath = documentsDir.path + "/buganddrug.db"

    return Room.databaseBuilder<LocalDatabase>(
        name = dbPath
    )
        .setDriver(BundledSQLiteDriver())
}