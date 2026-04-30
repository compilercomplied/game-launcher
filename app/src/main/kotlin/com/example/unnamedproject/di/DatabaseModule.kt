package com.example.unnamedproject.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.unnamedproject.data.local.AppDatabase
import com.example.unnamedproject.data.local.GameMetadataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "unnamed_project.db"
        ).build()
    }

    @Provides
    fun provideGameMetadataDao(database: AppDatabase): GameMetadataDao {
        return database.gameMetadataDao()
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
