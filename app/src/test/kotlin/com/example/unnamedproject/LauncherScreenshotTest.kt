package com.example.unnamedproject

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.example.unnamedproject.features.launcher.LauncherContent
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
class LauncherScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository = MockedGameRepository()

    @Test
    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherPortrait() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherMainContent(
                games = games,
                selectedIndex = 0,
                onGameSelected = {},
                onOpenDrawer = {}
            )
        }
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Config(qualifiers = "w640dp-h360dp-land")
    fun captureLauncherLandscape() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherMainContent(
                games = games,
                selectedIndex = 0,
                onGameSelected = {},
                onOpenDrawer = {}
            )
        }
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().captureRoboImage()
    }
}
