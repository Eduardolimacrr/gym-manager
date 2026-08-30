package com.example.gymmobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** Raios do protótipo, por papel. */
object GymShape {
    val cardLarge = RoundedCornerShape(16.dp)   // .today-card
    val card = RoundedCornerShape(14.dp)        // .stat-card, .template-card, .exercise-card
    val button = RoundedCornerShape(11.dp)      // .btn, .text-input
    val iconButton = RoundedCornerShape(10.dp)  // .icon-btn
    val miniButton = RoundedCornerShape(9.dp)   // .mini-btn
    val input = RoundedCornerShape(8.dp)        // .set-row input, .check-toggle
    val pill = RoundedCornerShape(20.dp)        // .chip
}
