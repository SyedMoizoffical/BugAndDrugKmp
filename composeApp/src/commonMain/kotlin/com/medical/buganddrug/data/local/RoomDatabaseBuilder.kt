package com.medical.buganddrug.data.local

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<LocalDatabase>
