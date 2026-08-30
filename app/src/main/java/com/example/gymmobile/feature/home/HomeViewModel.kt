package com.example.gymmobile.feature.home

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.WorkoutSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    data class UiState(
        val greetingName: String,
        val streakValue: String,
        val weekVolume: String,
        val todayTag: String,
        val todayTitle: String,
        val todaySubtitle: String,
        val todayTemplateId: String,
        val recent: List<WorkoutSummary>,
    )

    private val _uiState = MutableStateFlow(
        UiState(
            greetingName = FakeRepository.userName,
            streakValue = FakeRepository.streakDays.toString(),
            weekVolume = FakeRepository.weekVolume,
            todayTag = FakeRepository.todayTag,
            todayTitle = FakeRepository.todayTitle,
            todaySubtitle = FakeRepository.todaySubtitle,
            todayTemplateId = FakeRepository.todayTemplateId,
            recent = FakeRepository.recentWorkouts,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
