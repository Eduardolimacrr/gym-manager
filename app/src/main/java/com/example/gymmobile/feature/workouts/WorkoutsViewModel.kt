package com.example.gymmobile.feature.workouts

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.WorkoutTemplate
import kotlinx.coroutines.flow.StateFlow

class WorkoutsViewModel : ViewModel() {
    /** Vem direto do repositório: um treino salvo aparece aqui sem recarregar a tela. */
    val templates: StateFlow<List<WorkoutTemplate>> = FakeRepository.templates
}
