package com.example.gymmobile.feature.progress

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.BodyMeasure
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.WeightEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BodyMetricsViewModel : ViewModel() {
    private val _weightHistory = MutableStateFlow(FakeRepository.weightHistory)
    val weightHistory: StateFlow<List<WeightEntry>> = _weightHistory.asStateFlow()

    private val _measures = MutableStateFlow(FakeRepository.currentBodyMeasures)
    val measures: StateFlow<List<BodyMeasure>> = _measures.asStateFlow()

    private val _bodyFat = MutableStateFlow(FakeRepository.bodyFat)
    val bodyFat: StateFlow<String> = _bodyFat.asStateFlow()
}
