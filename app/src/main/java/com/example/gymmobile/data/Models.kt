package com.example.gymmobile.data

/** Um treino salvo, exibido em Meus treinos. */
data class WorkoutTemplate(
    val id: String,
    val name: String,
    val tag: String,
    val exerciseCount: Int,
)

/** Uma série: carga, repetições e se já foi concluída. */
data class ExerciseSet(
    val weight: Double,
    val reps: Int,
    val done: Boolean = false,
)

/** Um exercício dentro do treino em execução. */
data class ActiveExercise(
    val name: String,
    val sets: List<ExerciseSet>,
)

/** Uma linha do histórico: `18 ago` / `80kg × 8`. */
data class HistoryEntry(
    val date: String,
    val value: String,
)

/** Um exercício acompanhado na tela de Progresso. */
data class ProgressExercise(
    val id: String,
    val name: String,
    val pr: String,
    val bars: List<Int>,
    val history: List<HistoryEntry>,
)

/** Um treino concluído, listado em "Últimos treinos". */
data class WorkoutSummary(
    val name: String,
    val day: String,
    val duration: String,
)
