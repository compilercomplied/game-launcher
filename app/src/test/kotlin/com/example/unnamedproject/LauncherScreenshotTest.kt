package com.example.unnamedproject

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.unnamedproject.features.launcher.LauncherScreen
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
class LauncherScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockCoverPath = java.io.File("src/test/resources/mock_cover.png").absolutePath
    private val games = listOf(
        com.example.unnamedproject.models.Game("Elden Ring", "com.software.elden", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Hades II", "com.supergiant.hades2", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Animal Crossing", "com.nintendo.ac", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Doom Eternal", "com.id.doom", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Tetris Effect", "com.enhance.tetris", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Cyberpunk 2077", "com.cdpr.cp2077", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("The Witcher 3", "com.cdpr.witcher3", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Outer Wilds", "com.mobius.outerwilds", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Balatro", "com.localthunk.balatro", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Stardew Valley", "com.chapefish.stardew", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Celeste", "com.maddy.celeste", Icons.Default.PlayArrow, coverPath = mockCoverPath),
        com.example.unnamedproject.models.Game("Default Game", "com.example.default", Icons.Default.PlayArrow)
    )

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureLauncherPortrait() {
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
    @Config(qualifiers = "w640dp-h360dp-land")
    fun captureLauncherLandscape() {
        composeTestRule.setContent {
            LauncherContent(
                games = games,
                selectedIndex = 0,
                onGameSelected = {}
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
