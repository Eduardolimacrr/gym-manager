package com.example.gymmobile.ui.theme

import androidx.compose.ui.graphics.Color

/** Tradução literal do bloco `:root` do protótipo HTML. */
object GymColors {
    val Bg = Color(0xFF0E0F13)
    val Surface = Color(0xFF1C1F26)
    val Surface2 = Color(0xFF242832)
    val Raised = Color(0xFF2A2E38)
    val BorderColor = Color(0xFF31353F)

    val TextPrimary = Color(0xFFF2F1ED)
    val TextSecondary = Color(0xFF9A9CA8)
    val TextMuted = Color(0xFF5F626D)

    val Accent = Color(0xFFFF6A39)
    val AccentDim = Accent.copy(alpha = 0.14f)
    val AccentText = Color(0xFFFF8B5F)

    val Success = Color(0xFF7FB069)
    val SuccessDim = Success.copy(alpha = 0.16f)

    val Chalk = Color(0xFFE9DCC0)

    /** Texto sobre `Accent` (`.btn-primary` do CSS). */
    val OnAccent = Color(0xFF1A0A04)

    /** Fundo do overlay de descanso (`rgba(10,11,14,0.92)`). */
    val Scrim = Color(0xFF0A0B0E).copy(alpha = 0.92f)
}
