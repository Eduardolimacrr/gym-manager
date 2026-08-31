package com.example.gymmobile.feature.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _elapsedMillis = MutableStateFlow(0L)
    val elapsedMillis: StateFlow<Long> = _elapsedMillis.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null

    fun toggleTimer() {
        if (_isRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _isRunning.value = true
        val startTime = System.currentTimeMillis() - _elapsedMillis.value
        timerJob = viewModelScope.launch {
            while (true) {
                _elapsedMillis.value = System.currentTimeMillis() - startTime
                delay(10)
            }
        }
    }

    private fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _elapsedMillis.value = 0
    }

    fun formatTime(totalMillis: Long): String {
        val minutes = (totalMillis / 1000) / 60
        val seconds = (totalMillis / 1000) % 60
        val centiseconds = (totalMillis % 1000) / 10
        return "%02d:%02d:%02d".format(minutes, seconds, centiseconds)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
