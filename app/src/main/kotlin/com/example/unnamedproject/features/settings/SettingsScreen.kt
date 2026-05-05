package com.example.unnamedproject.features.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unnamedproject.R
import com.example.unnamedproject.contracts.host.ThemeMode
import com.example.unnamedproject.contracts.host.ThemeSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var showUiSettings by remember { mutableStateOf(false) }
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (showUiSettings) "UI Settings" 
                        else stringResource(R.string.settings)
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (showUiSettings) showUiSettings = false
                            else onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!showUiSettings) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { showUiSettings = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("UI settings")
                    }
                }
            } else {
                UiSettingsForm(
                    settings = settings,
                    onUpdateRadius = viewModel::updateCornerRadius,
                    onUpdateWidth = viewModel::updateCoverWidth,
                    onUpdateScale = viewModel::updateSelectedScale,
                    onUpdateUseDynamicColor = viewModel::updateUseDynamicColor,
                    onUpdateThemeMode = viewModel::updateThemeMode
                )
            }
        }
    }
}

@Composable
fun UiSettingsForm(
    settings: ThemeSettings?,
    onUpdateRadius: (Float) -> Unit,
    onUpdateWidth: (Float) -> Unit,
    onUpdateScale: (Float) -> Unit,
    onUpdateUseDynamicColor: (Boolean) -> Unit,
    onUpdateThemeMode: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Appearance",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dynamic Color", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = settings?.useDynamicColor ?: true,
                onCheckedChange = onUpdateUseDynamicColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Theme Mode", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    RadioButton(
                        selected = (settings?.themeMode ?: ThemeMode.FOLLOW_SYSTEM) == mode,
                        onClick = { onUpdateThemeMode(mode) }
                    )
                    Text(text = mode.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "game cover",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Corner Radius
        SettingSlider(
            label = "Corner Radius",
            value = settings?.cornerRadiusDp ?: 28f,
            range = 0f..64f,
            onValueChange = onUpdateRadius,
            valueDisplay = { "${it.toInt()} dp" }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Cover Width
        SettingSlider(
            label = "Cover Width",
            value = settings?.coverWidthDp ?: 100f,
            range = 50f..300f,
            onValueChange = onUpdateWidth,
            valueDisplay = { "${it.toInt()} dp" }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Selected Scale
        SettingSlider(
            label = "Selected Scale",
            value = settings?.selectedScale ?: 1.15f,
            range = 1.0f..1.5f,
            onValueChange = onUpdateScale,
            valueDisplay = { "%.2f x".format(it) }
        )
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    valueDisplay: (Float) -> String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = valueDisplay(value),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
