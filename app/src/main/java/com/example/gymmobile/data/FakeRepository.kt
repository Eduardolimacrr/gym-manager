package com.example.gymmobile.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Fonte de dados única do app, em memória.
 *
 * É um `object` para que o estado sobreviva à troca de telas: um treino
 * salvo em Novo treino precisa aparecer imediatamente em Meus treinos,
 * como acontecia no protótipo com o array global `templates`.
 */
object FakeRepository {

    // ----- perfil -----
    const val userName = "Lucas"
    const val userFullName = "Lucas Costa"
    const val userInitials = "LC"
    const val userSubtitle = "Treinando há 8 meses"

    // ----- home -----
    const val streakDays = 4
    const val weekVolume = "12,4"
    const val todayTag = "Push · dia 3"
    const val todayTitle = "Peito, ombro e tríceps"
    const val todaySubtitle = "5 exercícios · ~50 min"

    /** Template aberto pelo botão "Iniciar treino" da Home. */
    const val todayTemplateId = "push-a"

    val recentWorkouts = listOf(
        WorkoutSummary("Pull B", "seg", "48min"),
        WorkoutSummary("Legs", "sáb", "55min"),
        WorkoutSummary("Push A", "qui", "51min"),
    )

    // ----- treinos -----
    private val _templates = MutableStateFlow(
        listOf(
            WorkoutTemplate("push-a", "Push A", "Peito · Ombro · Tríceps", 5),
            WorkoutTemplate("pull-b", "Pull B", "Costas · Bíceps", 6),
            WorkoutTemplate("legs", "Legs", "Pernas · Glúteos", 6),
            WorkoutTemplate("full-body", "Full body", "Corpo inteiro", 8),
        )
    )
    val templates: StateFlow<List<WorkoutTemplate>> = _templates.asStateFlow()

    fun templateById(id: String): WorkoutTemplate? =
        _templates.value.firstOrNull { it.id == id }

    /**
     * Regras copiadas de `saveWorkout()` do protótipo: nome em branco vira
     * "Treino sem nome"; a tag são os três primeiros exercícios unidos por
     * " · ", ou "Sem exercícios" quando nada foi escolhido.
     */
    fun addTemplate(name: String, picked: List<String>) {
        val finalName = name.trim().ifBlank { "Treino sem nome" }
        val tag = if (picked.isEmpty()) "Sem exercícios" else picked.take(3).joinToString(" · ")
        val id = "custom-${System.currentTimeMillis()}"
        _templates.value = _templates.value + WorkoutTemplate(id, finalName, tag, picked.size)
    }

    // ----- treino ativo -----
    /**
     * Lista única, devolvida para qualquer template: o protótipo também não
     * tinha exercícios por treino. O que muda por template é só o nome
     * exibido no rótulo da tela.
     */
    val activeExercises = listOf(
        ActiveExercise(
            "Supino reto",
            listOf(ExerciseSet(60.0, 10, done = true), ExerciseSet(60.0, 8)),
        ),
        ActiveExercise(
            "Desenvolvimento halteres",
            listOf(ExerciseSet(18.0, 12), ExerciseSet(18.0, 10)),
        ),
        ActiveExercise(
            "Elevação lateral",
            listOf(ExerciseSet(8.0, 15)),
        ),
    )

    // ----- progresso -----
    const val sessionsCount = 18

    val progressExercises = listOf(
        ProgressExercise(
            id = "supino-reto",
            name = "Supino reto",
            pr = "80kg",
            bars = listOf(40, 55, 60, 80, 95, 100),
            history = listOf(
                HistoryEntry("18 ago", "80kg × 8"),
                HistoryEntry("14 ago", "75kg × 10"),
                HistoryEntry("10 ago", "75kg × 8"),
            ),
        ),
        ProgressExercise(
            id = "agachamento-livre",
            name = "Agachamento livre",
            pr = "110kg",
            bars = listOf(50, 60, 70, 85, 90, 100),
            history = listOf(
                HistoryEntry("17 ago", "110kg × 6"),
                HistoryEntry("12 ago", "100kg × 8"),
            ),
        ),
        ProgressExercise(
            id = "levantamento-terra",
            name = "Levantamento terra",
            pr = "140kg",
            bars = listOf(60, 65, 75, 80, 92, 100),
            history = listOf(
                HistoryEntry("15 ago", "140kg × 4"),
                HistoryEntry("8 ago", "130kg × 5"),
            ),
        ),
    )

    fun progressById(id: String): ProgressExercise? =
        progressExercises.firstOrNull { it.id == id }

    // ----- catálogo do criador de treinos -----
    val exerciseCatalog = listOf(
        "Supino reto", "Supino inclinado", "Desenvolvimento halteres", "Elevação lateral",
        "Tríceps corda", "Puxada frente", "Remada curvada", "Rosca direta",
        "Agachamento livre", "Leg press", "Cadeira extensora", "Levantamento terra",
    )
}
