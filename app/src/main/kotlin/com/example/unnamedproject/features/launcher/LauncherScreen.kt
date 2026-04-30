package com.example.unnamedproject.features.launcher

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.unnamedproject.R
import com.example.unnamedproject.features.launcher.components.GameItem
import com.example.unnamedproject.models.Game

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.foundation.ExperimentalFoundationApi
import kotlin.math.abs

@Composable
fun LauncherScreen(viewModel: LauncherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    LauncherContent(
        games = uiState.games,
        selectedIndex = uiState.selectedIndex,
        onGameSelected = { viewModel.onGameSelected(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LauncherContent(
    games: List<Game>,
    selectedIndex: Int,
    onGameSelected: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val listState = rememberLazyListState()

    // Calculate which item is closest to the center
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) 0
            else {
                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItems.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: 0
            }
        }
    }

    LaunchedEffect(centerIndex) {
        onGameSelected(centerIndex)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

                if (isLandscape) {
                    val itemWidth = 100.dp
                    val horizontalPadding = maxWidth / 2 - itemWidth / 2
                    LazyRow(
                        state = listState,
                        flingBehavior = snapFlingBehavior,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = horizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(games.size) { index ->
                            GameItem(
                                game = games[index],
                                isSelected = index == selectedIndex
                            )
                        }
                    }
                } else {
                    val itemHeight = 150.dp
                    val verticalPadding = maxHeight / 2 - itemHeight / 2
                    LazyColumn(
                        state = listState,
                        flingBehavior = snapFlingBehavior,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = verticalPadding),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(games.size) { index ->
                            GameItem(
                                game = games[index],
                                isSelected = index == selectedIndex
                            )
                        }
                    }
                }
            }
        }
    }
}
