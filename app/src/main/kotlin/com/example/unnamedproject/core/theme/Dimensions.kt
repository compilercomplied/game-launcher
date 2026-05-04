package com.example.unnamedproject.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val gameCoverWidth: Dp = 100.dp,
    val gameCoverHeight: Dp = 150.dp,
    val gameCoverSelectedScale: Float = 1.15f,
    val gameCoverCornerRadius: Dp = 28.dp
)

val LocalAppDimensions = staticCompositionLocalOf { AppDimensions() }