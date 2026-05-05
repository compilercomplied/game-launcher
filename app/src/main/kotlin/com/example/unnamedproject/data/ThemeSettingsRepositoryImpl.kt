package com.example.unnamedproject.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.unnamedproject.contracts.host.ThemeMode
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
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CORNER_RADIUS = floatPreferencesKey("corner_radius")
        val COVER_WIDTH = floatPreferencesKey("cover_width")
        val SELECTED_SCALE = floatPreferencesKey("selected_scale")
    }

    override val settings: Flow<ThemeSettings> = dataStore.data.map { preferences ->
        val themeModeStr = preferences[PreferencesKeys.THEME_MODE]
        val themeMode = try {
            if (themeModeStr != null) ThemeMode.valueOf(themeModeStr) else ThemeMode.FOLLOW_SYSTEM
        } catch (e: IllegalArgumentException) {
            ThemeMode.FOLLOW_SYSTEM
        }
        
        ThemeSettings(
            useDynamicColor = preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true,
            themeMode = themeMode,
            cornerRadiusDp = preferences[PreferencesKeys.CORNER_RADIUS],
            coverWidthDp = preferences[PreferencesKeys.COVER_WIDTH],
            selectedScale = preferences[PreferencesKeys.SELECTED_SCALE]
        )
    }

    override suspend fun updateUseDynamicColor(useDynamic: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = useDynamic
        }
    }

    override suspend fun updateThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
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
