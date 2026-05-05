package com.example.unnamedproject.contracts.host

import kotlinx.coroutines.flow.Flow

enum class ThemeMode { FOLLOW_SYSTEM, LIGHT, DARK }

data class ThemeSettings(
    val useDynamicColor: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.FOLLOW_SYSTEM,
    val cornerRadiusDp: Float? = null,
    val coverWidthDp: Float? = null,
    val selectedScale: Float? = null
)

interface ThemeSettingsRepository {
    val settings: Flow<ThemeSettings>
    suspend fun updateUseDynamicColor(useDynamic: Boolean)
    suspend fun updateThemeMode(mode: ThemeMode)
    suspend fun updateCornerRadius(radius: Float)
    suspend fun updateCoverWidth(width: Float)
    suspend fun updateSelectedScale(scale: Float)
}
