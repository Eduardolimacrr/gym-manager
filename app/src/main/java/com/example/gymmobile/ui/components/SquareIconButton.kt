package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape

/** Cobre `.icon-btn` (34dp, raio 10) e `.mini-btn` (32dp, raio 9). */
@Composable
fun SquareIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
    size: Dp = 34.dp,
    shape: Shape = GymShape.iconButton,
    /** `.icon-btn` usa `--surface`; `.mini-btn` passa `--surface-2` aqui. */
    container: Color = GymColors.Surface,
) {
    val background = if (accent) GymColors.Accent else container
    val border = if (accent) GymColors.Accent else GymColors.BorderColor
    val tint = if (accent) GymColors.OnAccent else GymColors.TextPrimary

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background)
            .border(0.5.dp, border, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(if (size <= 32.dp) 14.dp else 17.dp),
        )
    }
}
