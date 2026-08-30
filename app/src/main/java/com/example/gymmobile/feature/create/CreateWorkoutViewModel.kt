package com.example.gymmobile.feature.create

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateWorkoutViewModel : ViewModel() {

    data class UiState(
        val name: String = "",
        val catalog: List<String> = FakeRepository.exerciseCatalog,
        val picked: List<String> = emptyList(),
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    /** `toggleChip` do protótipo: alterna, preservando a ordem de escolha. */
    fun toggleExercise(name: String) {
        _uiState.update { state ->
            val picked =
                if (name in state.picked) state.picked - name
                else state.picked + name
            state.copy(picked = picked)
        }
    }

    fun save() {
        val state = _uiState.value
        FakeRepository.addTemplate(name = state.name, picked = state.picked)
    }
}
