package com.example.gymmobile.feature.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    data class UiState(
        val unit: String = "kg",
        val restNotifications: Boolean = true,
        val workoutReminder: Boolean = false,
        val autoBackup: Boolean = true,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setUnit(value: String) = _uiState.update { it.copy(unit = value) }
    fun setRestNotifications(value: Boolean) = _uiState.update { it.copy(restNotifications = value) }
    fun setWorkoutReminder(value: Boolean) = _uiState.update { it.copy(workoutReminder = value) }
    fun setAutoBackup(value: Boolean) = _uiState.update { it.copy(autoBackup = value) }
}
