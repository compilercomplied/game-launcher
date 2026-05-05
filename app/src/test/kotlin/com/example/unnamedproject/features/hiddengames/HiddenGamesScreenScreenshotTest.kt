package com.example.unnamedproject.features.hiddengames

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.unnamedproject.models.Game
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w360dp-h640dp-port")
class HiddenGamesScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun captureHiddenGamesScreen_Empty() {
        val viewModel: HiddenGamesViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(HiddenGamesUiState())

        composeTestRule.setContent {
            HiddenGamesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun captureHiddenGamesScreen_WithGames() {
        val games = listOf(
            Game("Game 1", "com.example.game1", Icons.Default.Gamepad, isHidden = true),
            Game("Game 2", "com.example.game2", Icons.Default.Gamepad, isHidden = true)
        )
        val viewModel: HiddenGamesViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(HiddenGamesUiState(games))

        composeTestRule.setContent {
            HiddenGamesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
