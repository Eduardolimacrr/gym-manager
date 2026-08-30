package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/** `.btn.btn-primary` — laranja, largura total. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    ButtonBase(
        text = text,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        background = GymColors.Accent,
        contentColor = GymColors.OnAccent,
        borderColor = null,
    )
}

/** `.btn.btn-ghost` — superfície escura com borda fina. */
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    ButtonBase(
        text = text,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        background = GymColors.Surface2,
        contentColor = GymColors.TextPrimary,
        borderColor = GymColors.BorderColor,
    )
}

@Composable
private fun ButtonBase(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    icon: ImageVector?,
    background: Color,
    contentColor: Color,
    borderColor: Color?,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(GymShape.button)
            .background(background)
            .then(
                if (borderColor != null) Modifier.border(0.5.dp, borderColor, GymShape.button)
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(15.dp),
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = GymType.body14.copy(color = contentColor, fontWeight = FontWeight.Medium),
        )
    }
}
