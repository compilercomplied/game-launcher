package com.example.unnamedproject.features.launcher.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.unnamedproject.models.Game
import java.io.File

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.material3.ripple
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CancellationException

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

import com.example.unnamedproject.core.e2eTag
import com.example.unnamedproject.core.theme.LocalAppDimensions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameItem(
    game: Game,
    index: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
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

    val dimensions = LocalAppDimensions.current

    val scale by animateFloatAsState(
        targetValue = if (isSelected) dimensions.gameCoverSelectedScale else 1.0f,
        label = "selection_scale"
    )

    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(dimensions.gameCoverWidth)
            .scale(scale)
    ) {
        val shape = RoundedCornerShape(dimensions.gameCoverCornerRadius)
        Box(
            modifier = Modifier
                .width(dimensions.gameCoverWidth)
                .height(dimensions.gameCoverHeight)
                .clip(shape)
                .indication(interactionSource, ripple())
                .pointerInput(interactionSource) {
                    detectTapGestures(
                        onPress = { offset ->
                            val press = PressInteraction.Press(offset)
                            interactionSource.emit(press)
                            try {
                                awaitRelease()
                                interactionSource.emit(PressInteraction.Release(press))
                            } catch (c: CancellationException) {
                                interactionSource.emit(PressInteraction.Cancel(press))
                            }
                        },
                        onTap = { onClick() },
                        onLongPress = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onLongClick()
                        }
                    )
                }
                .e2eTag(
                    id = "game_item",
                    "index" to index,
                    "selected" to isSelected,
                    "loaded" to (coverBitmap != null)
                )
                .semantics { 
                    selected = isSelected 
                    role = Role.Button
                    onClick { 
                        onClick()
                        true
                    }
                }
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
                        .e2eTag("game_cover_downloaded"),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = game.icon,
                    contentDescription = game.name,
                    modifier = Modifier
                        .size(48.dp)
                        .e2eTag("game_icon_default"),
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