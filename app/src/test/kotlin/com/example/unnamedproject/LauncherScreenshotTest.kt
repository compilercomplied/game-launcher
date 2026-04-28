package com.example.unnamedproject

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.unnamedproject.features.launcher.LauncherScreen
import com.example.unnamedproject.features.launcher.LauncherContent
import com.example.unnamedproject.mocks.MockedGameRepository
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class LauncherScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockedRepository = MockedGameRepository()
    private val games = mockedRepository.getInstalledGames()

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherPortrait() {
        composeTestRule.setContent {
            LauncherContent(games = games)
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    @Config(qualifiers = "w640dp-h360dp-land")
    fun captureLauncherLandscape() {
        composeTestRule.setContent {
            LauncherContent(games = games)
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
