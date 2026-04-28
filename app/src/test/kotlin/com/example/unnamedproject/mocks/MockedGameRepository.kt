package com.example.unnamedproject.mocks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game

class MockedGameRepository : GameRepository {
    override fun getInstalledGames(): List<Game> = listOf(
        Game("Elden Ring", "com.software.elden", Icons.Default.PlayArrow),
        Game("Hades II", "com.supergiant.hades2", Icons.Default.PlayArrow),
        Game("Animal Crossing", "com.nintendo.ac", Icons.Default.PlayArrow),
        Game("Doom Eternal", "com.id.doom", Icons.Default.PlayArrow),
        Game("Tetris Effect", "com.enhance.tetris", Icons.Default.PlayArrow)
    )
}
