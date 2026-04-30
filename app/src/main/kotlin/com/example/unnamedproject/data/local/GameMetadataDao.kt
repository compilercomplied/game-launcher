package com.example.unnamedproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameMetadataDao {
    @Query("SELECT * FROM game_metadata")
    suspend fun getAllMetadata(): List<GameMetadataEntity>

    @Query("SELECT * FROM game_metadata WHERE packageName = :packageName")
    suspend fun getMetadataForPackage(packageName: String): GameMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: GameMetadataEntity)

    @Query("DELETE FROM game_metadata WHERE packageName = :packageName")
    suspend fun deleteMetadataForPackage(packageName: String)
}
