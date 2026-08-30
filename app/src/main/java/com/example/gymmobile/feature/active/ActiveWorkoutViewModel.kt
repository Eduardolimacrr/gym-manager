package com.example.gymmobile.feature.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymmobile.data.ActiveExercise
import com.example.gymmobile.data.ExerciseSet
import com.example.gymmobile.data.FakeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ActiveWorkoutViewModel(templateId: String) : ViewModel() {

    /** Estado do descanso. `null` em `UiState.rest` significa overlay escondido. */
    data class RestState(val remaining: Int, val total: Int) {
        val progress: Float get() = if (total <= 0) 0f else remaining.toFloat() / total
    }

    data class UiState(
        val contextLabel: String,
        val elapsedLabel: String,
        val exercises: List<ActiveExercise>,
        val rest: RestState? = null,
    )

    private val templateName = FakeRepository.templateById(templateId)?.name ?: "Treino"

    private val _exercises = MutableStateFlow(FakeRepository.activeExercises)
    private val _elapsedSeconds = MutableStateFlow(0)
    private val _rest = MutableStateFlow<RestState?>(null)

    val uiState: StateFlow<UiState> =
        combine(_exercises, _elapsedSeconds, _rest) { exercises, elapsed, rest ->
            UiState(
                contextLabel = buildContextLabel(templateName, exercises),
                elapsedLabel = formatElapsed(elapsed),
                exercises = exercises,
                rest = rest,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(
                contextLabel = buildContextLabel(templateName, _exercises.value),
                elapsedLabel = formatElapsed(0),
                exercises = _exercises.value,
            ),
        )

    init {
        // Cronômetro da sessão. No protótipo era o texto fixo "00:04:12".
        viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                _elapsedSeconds.update { it + 1 }
            }
        }
    }

    // ----- séries -----

    fun onWeightChange(exerciseIndex: Int, setIndex: Int, raw: String) {
        val weight = raw.replace(',', '.').toDoubleOrNull() ?: 0.0
        updateSet(exerciseIndex, setIndex) { it.copy(weight = weight) }
    }

    fun onRepsChange(exerciseIndex: Int, setIndex: Int, raw: String) {
        val reps = raw.toIntOrNull() ?: 0
        updateSet(exerciseIndex, setIndex) { it.copy(reps = reps) }
    }

    /** `toggleSet`: marcar dispara o descanso; desmarcar apenas limpa. */
    fun toggleSetDone(exerciseIndex: Int, setIndex: Int) {
        val wasDone = _exercises.value[exerciseIndex].sets[setIndex].done
        updateSet(exerciseIndex, setIndex) { it.copy(done = !wasDone) }
        if (!wasDone) startRest(REST_SECONDS)
    }

    /** `addSet`: copia peso e reps da última série, sempre não concluída. */
    fun addSet(exerciseIndex: Int) {
        _exercises.update { exercises ->
            exercises.mapIndexed { index, exercise ->
                if (index != exerciseIndex) {
                    exercise
                } else {
                    val last = exercise.sets.lastOrNull() ?: ExerciseSet(0.0, 0)
                    exercise.copy(sets = exercise.sets + last.copy(done = false))
                }
            }
        }
    }

    private fun updateSet(
        exerciseIndex: Int,
        setIndex: Int,
        transform: (ExerciseSet) -> ExerciseSet,
    ) {
        _exercises.update { exercises ->
            exercises.mapIndexed { ei, exercise ->
                if (ei != exerciseIndex) {
                    exercise
                } else {
                    exercise.copy(
                        sets = exercise.sets.mapIndexed { si, set ->
                            if (si == setIndex) transform(set) else set
                        }
                    )
                }
            }
        }
    }

    // ----- descanso -----

    private var restJob: Job? = null

    private fun startRest(seconds: Int) {
        restJob?.cancel()
        _rest.value = RestState(remaining = seconds, total = seconds)
        restJob = viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                val current = _rest.value ?: break
                if (current.remaining <= 1) {
                    _rest.value = null
                    break
                }
                _rest.value = current.copy(remaining = current.remaining - 1)
            }
        }
    }

    /**
     * `adjustRest`: piso de 5s. O total acompanha para cima, para que o anel
     * nunca precise desenhar mais de uma volta.
     */
    fun adjustRest(deltaSeconds: Int) {
        val current = _rest.value ?: return
        val remaining = (current.remaining + deltaSeconds).coerceAtLeast(5)
        _rest.value = current.copy(
            remaining = remaining,
            total = maxOf(current.total, remaining),
        )
    }

    fun skipRest() {
        restJob?.cancel()
        _rest.value = null
    }

    companion object {
        const val REST_SECONDS = 60

        fun factory(templateId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer { ActiveWorkoutViewModel(templateId) }
        }

        /** 60.0 -> "60"; 62.5 -> "62,5" (vírgula, como se escreve em pt-BR). */
        fun formatWeight(value: Double): String =
            if (value % 1.0 == 0.0) value.toInt().toString()
            else value.toString().replace('.', ',')

        internal fun formatElapsed(totalSeconds: Int): String {
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            return "%02d:%02d:%02d".format(hours, minutes, seconds)
        }

        /**
         * `Push A · exercício 2 de 5` do protótipo, agora derivado: o índice é o
         * do primeiro exercício com série pendente, e o total é o tamanho real
         * da lista.
         */
        internal fun buildContextLabel(
            templateName: String,
            exercises: List<ActiveExercise>,
        ): String {
            val total = exercises.size
            if (total == 0) return templateName
            val pendingIndex = exercises.indexOfFirst { exercise ->
                exercise.sets.any { !it.done }
            }
            val current = if (pendingIndex == -1) total else (pendingIndex + 1).coerceAtMost(total)
            return "$templateName · exercício $current de $total"
        }
    }
}
