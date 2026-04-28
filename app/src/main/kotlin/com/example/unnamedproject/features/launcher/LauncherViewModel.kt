package com.example.unnamedproject.features.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Game>>(emptyList())
    val uiState: StateFlow<List<Game>> = _uiState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = repository.getInstalledGames()
        }
    }
}
