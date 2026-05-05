package com.example.unnamedproject.di

import android.content.Context
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.contracts.host.ThemeSettingsRepository
import com.example.unnamedproject.data.AndroidGameRepository
import com.example.unnamedproject.data.ThemeSettingsRepositoryImpl
import com.example.unnamedproject.data.local.GameMetadataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGameRepository(
        @ApplicationContext context: Context,
        metadataDao: GameMetadataDao
    ): GameRepository {
        return AndroidGameRepository(context, metadataDao)
    }

    @Provides
    @Singleton
    fun provideThemeSettingsRepository(
        repository: ThemeSettingsRepositoryImpl
    ): ThemeSettingsRepository {
        return repository
    }
}
