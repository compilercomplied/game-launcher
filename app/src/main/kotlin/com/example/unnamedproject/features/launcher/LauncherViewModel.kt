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

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val repository: GameRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Game>>(emptyList())
    val uiState: StateFlow<List<Game>> = _uiState.asStateFlow()

    init {
        loadGames()
        triggerMetadataSync()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = repository.getInstalledGames()
        }
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
