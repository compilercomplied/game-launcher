package com.example.unnamedproject.features.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game
import com.example.unnamedproject.sync.MetadataSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.update

data class LauncherUiState(
    val games: List<Game> = emptyList(),
    val selectedIndex: Int = 0,
    val longPressedGame: Game? = null
)

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val repository: GameRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        loadGames()
        triggerMetadataSync()
    }

    fun loadGames() {
        viewModelScope.launch {
            val games = repository.getInstalledGames().filter { !it.isHidden }
            _uiState.update { it.copy(games = games) }
        }
    }

    fun onGameSelected(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun onGameLongPressed(game: Game?) {
        _uiState.update { it.copy(longPressedGame = game) }
    }

    fun toggleFavorite(game: Game) {
        // Placeholder for toggle favorite
    }

    fun hideGame(game: Game) {
        viewModelScope.launch {
            repository.setGameHiddenStatus(game.packageName, true)
            onGameLongPressed(null) // dismiss the sheet
            loadGames() // reload list
        }
    }

    fun editMetadata(game: Game) {
        // Placeholder for edit metadata
    }

    fun launchGame(game: Game) {
        repository.launchGame(game.packageName)
    }

    private fun triggerMetadataSync() {
        val syncRequest = OneTimeWorkRequestBuilder<MetadataSyncWorker>().build()
        workManager.enqueue(syncRequest)
        
        // Listen for work completion to reload games
        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(syncRequest.id).collect { workInfo ->
                if (workInfo?.state?.isFinished == true) {
                    loadGames()
                }
            }
        }
    }
}
