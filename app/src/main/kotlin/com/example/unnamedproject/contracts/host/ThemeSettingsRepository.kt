package com.example.unnamedproject.contracts.host

import kotlinx.coroutines.flow.Flow

data class ThemeSettings(
    val cornerRadiusDp: Float? = null,
    val coverWidthDp: Float? = null,
    val selectedScale: Float? = null
)

interface ThemeSettingsRepository {
    val settings: Flow<ThemeSettings>
    suspend fun updateCornerRadius(radius: Float)
    suspend fun updateCoverWidth(width: Float)
    suspend fun updateSelectedScale(scale: Float)
}
