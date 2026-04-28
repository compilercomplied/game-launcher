package com.example.unnamedproject.contracts.host

import com.example.unnamedproject.models.Game

interface GameRepository {
    fun getInstalledGames(): List<Game>
}
