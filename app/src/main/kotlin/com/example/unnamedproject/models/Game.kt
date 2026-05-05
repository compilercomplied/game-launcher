package com.example.unnamedproject.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Game(
    val name: String,
    val packageName: String,
    val icon: ImageVector,
    val coverPath: String? = null,
    val bannerPath: String? = null,
    val isHidden: Boolean = false
)
