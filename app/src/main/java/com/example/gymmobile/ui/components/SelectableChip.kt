    package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/** `.chip` / `.chip.selected`. */
@Composable
fun SelectableChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        style = GymType.body13.copy(
            color = if (selected) GymColors.AccentText else GymColors.TextSecondary,
        ),
        modifier = Modifier
            .clip(GymShape.pill)
            .background(if (selected) GymColors.AccentDim else GymColors.Surface)
            .border(
                width = 0.5.dp,
                color = if (selected) GymColors.Accent else GymColors.BorderColor,
                shape = GymShape.pill,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 8.dp),
    )
}
