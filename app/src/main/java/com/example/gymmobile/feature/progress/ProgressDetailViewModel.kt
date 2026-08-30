package com.example.gymmobile.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.HistoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressDetailViewModel(exerciseId: String) : ViewModel() {

    data class UiState(
        val name: String,
        val pr: String,
        val bars: List<Int>,
        val sessions: String,
        val lastLoad: String,
        val history: List<HistoryEntry>,
    )

    private val _uiState: MutableStateFlow<UiState>
    val uiState: StateFlow<UiState>

    init {
        val exercise = FakeRepository.progressById(exerciseId)
            ?: FakeRepository.progressExercises.first()

        _uiState = MutableStateFlow(
            UiState(
                name = exercise.name,
                pr = exercise.pr,
                bars = exercise.bars,
                sessions = FakeRepository.sessionsCount.toString(),
                // `.val.split(' ')[0]` do protótipo: "80kg × 8" -> "80kg".
                lastLoad = exercise.history.firstOrNull()
                    ?.value?.substringBefore(' ')
                    .orEmpty(),
                history = exercise.history,
            )
        )
        uiState = _uiState.asStateFlow()
    }

    companion object {
        fun factory(exerciseId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer { ProgressDetailViewModel(exerciseId) }
        }
    }
}
