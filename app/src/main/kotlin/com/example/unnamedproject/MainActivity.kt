package com.example.unnamedproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unnamedproject.core.theme.AppTheme
import com.example.unnamedproject.features.launcher.LauncherScreen
import com.example.unnamedproject.features.launcher.LauncherViewModel
import com.example.unnamedproject.features.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val viewModel: LauncherViewModel = hiltViewModel()
                
                // We wrap the screen in a Surface with testTagsAsResourceId enabled.
                // This allows external E2E tools (like Maestro) to 'see' Compose testTags 
                // as standard Android Resource IDs, which are otherwise internal to Compose.
                Surface(
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                    }
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "launcher") {
                        composable("launcher") {
                            LauncherScreen(
                                viewModel = viewModel,
                                onNavigateToSettings = { navController.navigate("settings") }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}