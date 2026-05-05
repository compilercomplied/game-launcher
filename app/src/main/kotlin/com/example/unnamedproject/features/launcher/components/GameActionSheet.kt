package com.example.unnamedproject.features.launcher.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.example.unnamedproject.R
import com.example.unnamedproject.core.e2eTag
import com.example.unnamedproject.models.Game

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GameActionSheet(
    game: Game,
    onDismiss: () -> Unit,
    onFavorite: (Game) -> Unit,
    onHide: (Game) -> Unit,
    onEditMetadata: (Game) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .semantics { testTagsAsResourceId = true }
                .e2eTag("game_action_sheet_content")
        ) {
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            ListItem(
                headlineContent = { Text(stringResource(R.string.action_favorite)) },
                leadingContent = { 
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                modifier = Modifier
                    .clickable { onFavorite(game) }
                    .e2eTag("action_favorite"),
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
            
            ListItem(
                headlineContent = { Text(stringResource(R.string.action_hide)) },
                leadingContent = { 
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    ) 
                },
                modifier = Modifier
                    .clickable { onHide(game) }
                    .e2eTag("action_hide"),
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
            
            ListItem(
                headlineContent = { Text(stringResource(R.string.action_edit_metadata)) },
                leadingContent = { 
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    ) 
                },
                modifier = Modifier
                    .clickable { onEditMetadata(game) }
                    .e2eTag("action_edit_metadata"),
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}
