package com.example.unnamedproject.contracts.host

import com.example.unnamedproject.models.Game

interface GameRepository {
    suspend fun getInstalledGames(): List<Game>
    fun launchGame(packageName: String)
}
