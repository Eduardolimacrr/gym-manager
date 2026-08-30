package com.example.gymmobile.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors

/** `.toggle` — 42×24 com botão de 18 que desliza de 2dp a 20dp. */
@Composable
fun ToggleSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    val knobX by animateDpAsState(
        targetValue = if (checked) 20.dp else 2.dp,
        label = "knobX",
    )

    Box(
        modifier = Modifier
            .size(width = 42.dp, height = 24.dp)
            .clip(shape)
            .background(if (checked) GymColors.AccentDim else GymColors.Surface2)
            .border(0.5.dp, if (checked) GymColors.Accent else GymColors.BorderColor, shape)
            .clickable { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobX, y = 2.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) GymColors.Accent else GymColors.TextMuted),
        )
    }
}
