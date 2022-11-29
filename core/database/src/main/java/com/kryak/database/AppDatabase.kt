package com.kryak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kryak.database.model.Download

@Database(entities = [Download::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun downloadDao(): DownloadDAO
}
