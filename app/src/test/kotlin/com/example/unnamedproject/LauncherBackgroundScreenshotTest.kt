package com.example.unnamedproject

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.unnamedproject.features.launcher.LauncherContent
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
class LauncherBackgroundScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository = MockedGameRepository()

    @Test
    @Config(qualifiers = "w360dp-h640dp-port", sdk = [33]) // SDK 33 for blur effect
    fun captureLauncherBackgroundFirstItem() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherContent(
                games = games,
                selectedIndex = 0,
                onGameSelected = {}
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp-port", sdk = [33])
    fun captureLauncherBackgroundSecondItem() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherContent(
                games = games,
                selectedIndex = 1,
                onGameSelected = {}
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp-port", sdk = [26]) // Below Android S, no blur
    fun captureLauncherBackgroundNoBlur() = runBlocking {
        val games = repository.getInstalledGames()
        composeTestRule.setContent {
            LauncherContent(
                games = games,
                selectedIndex = 2,
                onGameSelected = {}
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
