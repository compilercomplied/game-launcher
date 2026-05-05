package com.example.unnamedproject.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.unnamedproject.contracts.host.ThemeSettings
import com.example.unnamedproject.contracts.host.ThemeSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ThemeSettingsRepository {

    private object PreferencesKeys {
        val CORNER_RADIUS = floatPreferencesKey("corner_radius")
        val COVER_WIDTH = floatPreferencesKey("cover_width")
        val SELECTED_SCALE = floatPreferencesKey("selected_scale")
    }

    override val settings: Flow<ThemeSettings> = dataStore.data.map { preferences ->
        ThemeSettings(
            cornerRadiusDp = preferences[PreferencesKeys.CORNER_RADIUS],
            coverWidthDp = preferences[PreferencesKeys.COVER_WIDTH],
            selectedScale = preferences[PreferencesKeys.SELECTED_SCALE]
        )
    }

    override suspend fun updateCornerRadius(radius: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CORNER_RADIUS] = radius
        }
    }

    override suspend fun updateCoverWidth(width: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COVER_WIDTH] = width
        }
    }

    override suspend fun updateSelectedScale(scale: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_SCALE] = scale
        }
    }
}
