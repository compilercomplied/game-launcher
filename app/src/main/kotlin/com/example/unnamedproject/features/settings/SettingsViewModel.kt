package com.example.unnamedproject.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unnamedproject.contracts.host.ThemeSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ThemeSettingsRepository
) : ViewModel() {

    val settings = repository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun updateCornerRadius(radius: Float) {
        viewModelScope.launch {
            repository.updateCornerRadius(radius)
        }
    }

    fun updateCoverWidth(width: Float) {
        viewModelScope.launch {
            repository.updateCoverWidth(width)
        }
    }

    fun updateSelectedScale(scale: Float) {
        viewModelScope.launch {
            repository.updateSelectedScale(scale)
        }
    }
}
