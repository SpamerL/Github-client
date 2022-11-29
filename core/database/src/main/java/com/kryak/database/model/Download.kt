package com.kryak.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Download(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val repositoryName: String,
    val owner: String
)
