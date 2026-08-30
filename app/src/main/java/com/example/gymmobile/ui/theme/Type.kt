package com.example.gymmobile.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.gymmobile.R

object GymFonts {
    /** `--font-display`: Oswald */
    val Display = FontFamily(
        Font(R.font.oswald_regular, FontWeight.Normal),
        Font(R.font.oswald_medium, FontWeight.Medium),
        Font(R.font.oswald_semibold, FontWeight.SemiBold),
        Font(R.font.oswald_bold, FontWeight.Bold),
    )

    /** `--font-body`: Inter */
    val Body = FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
    )

    /** `--font-mono`: JetBrains Mono */
    val Mono = FontFamily(
        Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
        Font(R.font.jetbrains_mono_medium, FontWeight.Medium),
    )
}

/** Estilos nomeados pelo papel que cumprem no protótipo, não pela escala do Material. */
object GymType {
    private val display = TextStyle(fontFamily = GymFonts.Display, color = GymColors.TextPrimary)
    private val body = TextStyle(fontFamily = GymFonts.Body, color = GymColors.TextPrimary)
    private val mono = TextStyle(fontFamily = GymFonts.Mono, color = GymColors.TextPrimary)

    // Display (Oswald)
    val display44 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 44.sp, lineHeight = 44.sp)
    val display40 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 40.sp)
    val display24 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
    val display22 = display.copy(fontWeight = FontWeight.Medium, fontSize = 22.sp)
    val display20 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 20.sp)  // .avatar
    val display19 = display.copy(fontWeight = FontWeight.Medium, fontSize = 19.sp)
    val display18 = display.copy(fontWeight = FontWeight.Medium, fontSize = 18.sp)    // .profile-header h3
    val display16 = display.copy(fontWeight = FontWeight.Medium, fontSize = 16.sp)

    // Mono (JetBrains Mono)
    val sectionLabel = mono.copy(
        fontSize = 12.sp,
        letterSpacing = 0.04.em,
        color = GymColors.TextSecondary,
    )
    val mono13 = mono.copy(fontSize = 13.sp)
    val mono12 = mono.copy(fontSize = 12.sp)
    val mono11 = mono.copy(fontSize = 11.sp)
    val mono10 = mono.copy(fontSize = 10.sp, letterSpacing = 0.03.em)

    // Body (Inter)
    val body15 = body.copy(fontSize = 15.sp)
    val body14 = body.copy(fontSize = 14.sp)
    val body13 = body.copy(fontSize = 13.sp)
    val body12 = body.copy(fontSize = 12.sp)
    val body11 = body.copy(fontSize = 11.sp)
}
