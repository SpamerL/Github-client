package com.kryak.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kryak.database.model.Download
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDAO {
    @Query("SELECT * FROM DOWNLOAD")
    fun getAll(): Flow<List<Download>>

    @Insert
    fun insertDownloadRecord(download: Download)
}
