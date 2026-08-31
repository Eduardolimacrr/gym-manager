package com.example.gymmobile.navigation

object Routes {
    const val HOME = "home"
    const val WORKOUTS = "workouts"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val CREATE = "create"
    const val METRICS = "metrics"
    const val TIMER = "timer"

    const val ARG_TEMPLATE_ID = "templateId"
    const val ARG_EXERCISE_ID = "exerciseId"

    const val ACTIVE = "active/{$ARG_TEMPLATE_ID}"
    const val PROGRESS_DETAIL = "progress/{$ARG_EXERCISE_ID}"

    fun active(templateId: String) = "active/$templateId"
    fun progressDetail(exerciseId: String) = "progress/$exerciseId"

    /** Rotas que mostram a barra inferior. */
    val TAB_ROUTES = listOf(HOME, WORKOUTS, PROGRESS, PROFILE)
}
