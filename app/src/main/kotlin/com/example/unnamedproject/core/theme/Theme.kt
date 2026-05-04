package com.example.unnamedproject.core.theme

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    val coverWidth = if (isLandscape) {
        (configuration.screenWidthDp / 5).dp
    } else {
        (configuration.screenWidthDp / 3).dp
    }
    
    val coverHeight = coverWidth * 1.5f
    
    val dimensions = AppDimensions(
        gameCoverWidth = coverWidth,
        gameCoverHeight = coverHeight,
        gameCoverSelectedScale = 1.15f
    )

    CompositionLocalProvider(LocalAppDimensions provides dimensions) {
        MaterialTheme(
            content = content
        )
    }
}