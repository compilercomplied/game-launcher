package com.example.unnamedproject

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.unnamedproject.features.launcher.LauncherMainContent
import com.example.unnamedproject.mocks.MockedGameRepository
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.runBlocking
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

    private val repository = MockedGameRepository()

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherSelectionPortrait() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherMainContent(
                games = games,
                selectedIndex = 0,
                onGameSelected = {},
                onGameLaunched = {},
                onOpenDrawer = {}
            )
        }
        // Advance clock to ensure selection animations (scale) and background transitions are complete
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherSecondItemSelected() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherMainContent(
                games = games,
                selectedIndex = 1,
                onGameSelected = {},
                onGameLaunched = {},
                onOpenDrawer = {}
            )
        }
        // Advance clock to ensure selection animations (scale) and background transitions are complete
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().captureRoboImage()
    }
}
