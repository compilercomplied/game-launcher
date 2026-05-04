package com.example.unnamedproject.features.launcher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w360dp-h640dp-port")
class LauncherNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickingSettingsInDrawer_triggersNavigationCallback() {
        var navigated = false
        composeTestRule.setContent {
            LauncherContent(
                games = emptyList(),
                selectedIndex = 0,
                onGameSelected = {},
                onGameLaunched = {},
                onNavigateToSettings = { navigated = true }
            )
        }

        // Open the drawer
        composeTestRule.onNodeWithContentDescription("Open navigation menu").performClick()
        
        // Advance clock to let drawer animation finish
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.waitForIdle()

        // Verify "Settings" is displayed (ensures drawer is open)
        composeTestRule.onNodeWithTag("settings_drawer_item").assertIsDisplayed().performClick()
        
        // Advance clock to allow click to be processed
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.waitForIdle()

        assertTrue(navigated, "onNavigateToSettings callback was not triggered")
    }
}