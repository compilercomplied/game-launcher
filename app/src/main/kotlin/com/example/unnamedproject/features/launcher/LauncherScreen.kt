package com.example.unnamedproject.features.launcher

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.unnamedproject.R
import com.example.unnamedproject.features.launcher.components.GameItem
import com.example.unnamedproject.models.Game
import java.io.File
import kotlin.math.abs
import kotlinx.coroutines.launch

@Composable
fun LauncherScreen(viewModel: LauncherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    LauncherContent(
        games = uiState.games,
        selectedIndex = uiState.selectedIndex,
        onGameSelected = { viewModel.onGameSelected(it) }
    )
}

@Composable
fun DynamicGameBackground(selectedGame: Game?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                testTag = "launcher_background_container"
            }
    ) {
        Crossfade(
            targetState = selectedGame,
            animationSpec = tween(700),
            label = "background_crossfade"
        ) { game ->
            val bitmap = remember(game?.bannerPath ?: game?.coverPath) {
                val path = game?.bannerPath ?: game?.coverPath
                path?.let {
                    val file = File(it)
                    if (file.exists()) {
                        BitmapFactory.decodeFile(it)?.asImageBitmap()
                    } else null
                }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("launcher_background_image")
                        .then(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                Modifier.blur(30.dp)
                            } else Modifier
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .semantics { 
                            testTag = "launcher_background_fallback"
                            contentDescription = "Background Fallback"
                        }
                )
            }

            // Scrim for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Black.copy(alpha = 0.6f),
                            0.3f to Color.Transparent,
                            0.7f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.8f)
                        )
                    )
                    .semantics {
                        testTag = "launcher_background_scrim"
                        contentDescription = "Background Scrim"
                    }
            )
        }
    }
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
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(stringResource(R.string.settings)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            DynamicGameBackground(
                selectedGame = if (games.isNotEmpty() && selectedIndex in games.indices) games[selectedIndex] else null
            )

            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.app_name)) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.menu_content_description)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                }
            ) { paddingValues ->
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
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
}
