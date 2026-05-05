package com.example.unnamedproject.features.launcher.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.unnamedproject.core.theme.AppTheme
import com.example.unnamedproject.models.Game
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class GameActionSheetScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    @Config(qualifiers = "w360dp-h640dp-port")
    fun captureGameActionSheet_LightMode() {
        val game = Game(
            packageName = "com.test.game",
            name = "Test Game",
            icon = Icons.Default.PlayArrow
        )

        composeTestRule.setContent {
            AppTheme {
                GameActionSheet(
                    game = game,
                    onDismiss = {},
                    onFavorite = {},
                    onHide = {},
                    onEditMetadata = {}
                )
            }
        }
        
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("game_action_sheet_content").captureRoboImage()
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp-port-night")
    fun captureGameActionSheet_DarkMode() {
        val game = Game(
            packageName = "com.test.game",
            name = "Test Game",
            icon = Icons.Default.PlayArrow
        )

        composeTestRule.setContent {
            AppTheme {
                GameActionSheet(
                    game = game,
                    onDismiss = {},
                    onFavorite = {},
                    onHide = {},
                    onEditMetadata = {}
                )
            }
        }
        
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("game_action_sheet_content").captureRoboImage()
    }
}
