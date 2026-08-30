package com.example.gymmobile.feature.progress

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.ProgressExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel : ViewModel() {
    private val _exercises = MutableStateFlow(FakeRepository.progressExercises)
    val exercises: StateFlow<List<ProgressExercise>> = _exercises.asStateFlow()
}
