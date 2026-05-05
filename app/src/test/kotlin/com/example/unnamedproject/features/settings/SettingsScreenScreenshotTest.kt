package com.example.unnamedproject.features.settings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.unnamedproject.contracts.host.ThemeSettings

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SettingsScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureSettingsScreen() {
        val viewModel: SettingsViewModel = mockk(relaxed = true)
        every { viewModel.settings } returns MutableStateFlow(ThemeSettings())

        composeTestRule.setContent {
            SettingsScreen(
                onNavigateBack = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
