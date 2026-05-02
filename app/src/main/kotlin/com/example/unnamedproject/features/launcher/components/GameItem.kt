package com.example.unnamedproject.features.launcher.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import com.example.unnamedproject.models.Game
import java.io.File

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.selection.selectable

@Composable
fun GameItem(
    game: Game,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val coverBitmap = remember(game.coverPath) {
        game.coverPath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                BitmapFactory.decodeFile(path)?.asImageBitmap()
            } else null
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        label = "selection_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(IntrinsicSize.Min)
            .scale(scale)
            .selectable(
                selected = isSelected,
                onClick = {},
                enabled = true
            )
            .testTag("game_item")
    ) {
        val shape = MaterialTheme.shapes.extraLarge
        Box(
            modifier = Modifier
                .size(100.dp)
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = shape
                        )
                    } else Modifier
                )
                .background(MaterialTheme.colorScheme.primaryContainer, shape = shape),
            contentAlignment = Alignment.Center
        ) {
            if (coverBitmap != null) {
                Image(
                    bitmap = coverBitmap,
                    contentDescription = game.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("game_cover_downloaded"),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = game.icon,
                    contentDescription = game.name,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("game_icon_default"),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = game.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }
}
