package com.example.unnamedproject.mocks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game
import java.io.File

class MockedGameRepository : GameRepository {
    override suspend fun getInstalledGames(): List<Game> = listOf(
        Game(
            "Elden Ring", 
            "com.software.elden", 
            Icons.Default.PlayArrow,
            coverPath = getMockPath("mock_cover_1.png"),
            bannerPath = getMockPath("mock_banner_1.png")
        ),
        Game(
            "Hades II", 
            "com.supergiant.hades2", 
            Icons.Default.PlayArrow,
            coverPath = getMockPath("mock_cover_2.png"),
            bannerPath = getMockPath("mock_banner_2.png")
        ),
        Game(
            "Animal Crossing", 
            "com.nintendo.ac", 
            Icons.Default.PlayArrow,
            coverPath = getMockPath("mock_cover_3.png"),
            bannerPath = getMockPath("mock_banner_3.png")
        ),
        Game(
            "Doom Eternal", 
            "com.id.doom", 
            Icons.Default.PlayArrow,
            coverPath = getMockPath("mock_cover_4.png"),
            bannerPath = getMockPath("mock_banner_4.png")
        ),
        Game(
            "Tetris Effect", 
            "com.enhance.tetris", 
            Icons.Default.PlayArrow,
            coverPath = getMockPath("mock_cover_5.png"),
            bannerPath = getMockPath("mock_banner_5.png")
        )
    )

    var lastLaunchedPackage: String? = null
        private set

    override fun launchGame(packageName: String) {
        lastLaunchedPackage = packageName
    }

    val hiddenStatuses = mutableMapOf<String, Boolean>()

    override suspend fun setGameHiddenStatus(packageName: String, isHidden: Boolean) {
        hiddenStatuses[packageName] = isHidden
    }

    private fun getMockPath(fileName: String): String {
        return File("src/test/resources/$fileName").absolutePath
    }
}
