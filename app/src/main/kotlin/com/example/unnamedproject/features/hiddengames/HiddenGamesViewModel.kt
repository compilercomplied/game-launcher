package com.example.unnamedproject.features.hiddengames

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HiddenGamesUiState(
    val hiddenGames: List<Game> = emptyList()
)

@HiltViewModel
class HiddenGamesViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HiddenGamesUiState())
    val uiState: StateFlow<HiddenGamesUiState> = _uiState.asStateFlow()

    init {
        loadHiddenGames()
    }

    private fun loadHiddenGames() {
        viewModelScope.launch {
            val games = repository.getInstalledGames().filter { it.isHidden }
            _uiState.update { it.copy(hiddenGames = games) }
        }
    }

    fun unhideGame(game: Game) {
        viewModelScope.launch {
            repository.setGameHiddenStatus(game.packageName, false)
            loadHiddenGames()
        }
    }

    fun openPlayStore(game: Game, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=${game.packageName}")
            setPackage("com.android.vending")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback if Play Store app is not available
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=${game.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }
}
