package com.example.unnamedproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameMetadataEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameMetadataDao(): GameMetadataDao
}
