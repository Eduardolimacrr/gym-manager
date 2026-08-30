package com.example.gymmobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GymColorScheme = darkColorScheme(
    primary = GymColors.Accent,
    onPrimary = GymColors.OnAccent,
    background = GymColors.Bg,
    onBackground = GymColors.TextPrimary,
    surface = GymColors.Surface,
    onSurface = GymColors.TextPrimary,
    outline = GymColors.BorderColor,
)

/**
 * Tema do app. Dark-only por decisão de spec: o protótipo não tem variante
 * clara, e `dynamicColor` destruiria a paleta laranja/giz.
 */
@Composable
fun GymTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = GymColorScheme, content = content)
}
