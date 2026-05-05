package com.example.unnamedproject.features.launcher.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import com.example.unnamedproject.models.Game

@OptIn(ExperimentalMaterial3Api::class)
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
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("game_action_sheet_content")) {
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            ListItem(
                headlineContent = { Text("Favorite") },
                modifier = Modifier.clickable { onFavorite(game) }
            )
            
            ListItem(
                headlineContent = { Text("Hide") },
                modifier = Modifier.clickable { onHide(game) }
            )
            
            ListItem(
                headlineContent = { Text("Edit Metadata") },
                modifier = Modifier.clickable { onEditMetadata(game) }
            )
        }
    }
}
