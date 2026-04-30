package com.example.unnamedproject

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.unnamedproject.features.launcher.LauncherContent
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class LauncherSelectionScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val games = listOf(
        com.example.unnamedproject.models.Game("Elden Ring", "com.software.elden", Icons.Default.PlayArrow),
        com.example.unnamedproject.models.Game("Hades II", "com.supergiant.hades2", Icons.Default.PlayArrow),
        com.example.unnamedproject.models.Game("Celeste", "com.mattmakesgames.celeste", Icons.Default.PlayArrow)
    )

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherSelectionPortrait() {
        composeTestRule.setContent {
            LauncherContent(
                games = games,
                selectedIndex = 0,
                onGameSelected = {}
            )
        }
        // Advance clock to ensure selection animations (scale) are complete
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherSecondItemSelected() {
        composeTestRule.setContent {
            LauncherContent(
                games = games,
                selectedIndex = 1,
                onGameSelected = {}
            )
        }
        // Advance clock to ensure selection animations (scale) are complete
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().captureRoboImage()
    }
}
