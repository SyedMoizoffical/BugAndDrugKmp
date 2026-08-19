package com.medical.buganddrug.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<LocalDatabase> {
    val appContext = (context as? Context) ?: throw IllegalArgumentException("Android Context required")
    val dbFile = appContext.getDatabasePath("buganddrug.db")
    return Room.databaseBuilder<LocalDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
