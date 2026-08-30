package com.example.gymmobile.feature.active

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/**
 * `.rest-overlay`. O anel substitui o `stroke-dasharray/dashoffset` do SVG por
 * um `drawArc` cujo `sweepAngle` acompanha o tempo restante.
 */
@Composable
fun RestOverlay(
    remaining: Int,
    progress: Float,
    onAdjust: (Int) -> Unit,
    onSkip: () -> Unit,
) {
    val sweep by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f) * 360f,
        animationSpec = tween(durationMillis = 300),
        label = "restSweep",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Scrim)
            // Bloqueia toques na tela de baixo enquanto o descanso está ativo.
            .pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "DESCANSO",
                style = GymType.sectionLabel,
                modifier = Modifier.padding(bottom = 20.dp),
            )

            Box(
                modifier = Modifier.size(180.dp).padding(bottom = 26.dp),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = 10.dp.toPx()
                    val inset = strokeWidth / 2f
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                    drawArc(
                        color = GymColors.BorderColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = Stroke(width = strokeWidth),
                    )
                    drawArc(
                        color = GymColors.Accent,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
                Text(text = remaining.toString(), style = GymType.display40)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                RestControl(text = "-15s", onClick = { onAdjust(-15) })
                RestControl(text = "+15s", onClick = { onAdjust(15) })
            }

            Text(
                text = "Pular descanso",
                style = GymType.body13.copy(
                    color = GymColors.TextMuted,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.clickable(onClick = onSkip),
            )
        }
    }
}

/** `.rest-controls button`. */
@Composable
private fun RestControl(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = GymType.mono13,
        modifier = Modifier
            .clip(GymShape.iconButton)
            .background(GymColors.Surface2)
            .border(0.5.dp, GymColors.BorderColor, GymShape.iconButton)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp),
    )
}
