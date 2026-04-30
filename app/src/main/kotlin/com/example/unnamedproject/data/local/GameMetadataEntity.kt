package com.example.unnamedproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_metadata")
data class GameMetadataEntity(
    @PrimaryKey val packageName: String,
    val coverPath: String?,
    val bannerPath: String?,
    val lastUpdated: Long
)
