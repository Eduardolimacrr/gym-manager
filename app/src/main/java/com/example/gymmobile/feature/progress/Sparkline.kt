package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors

/**
 * `.spark` — barras de 4dp, a última em destaque.
 * O CSS usa `height:${v/4}px` num contêiner de 28px; a divisão por 4 é mantida.
 */
@Composable
fun Sparkline(bars: List<Int>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(28.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        bars.forEachIndexed { index, value ->
            val isLast = index == bars.lastIndex
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height((value / 4f).dp)
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    .background(
                        if (isLast) GymColors.Accent
                        else GymColors.Chalk.copy(alpha = 0.5f)
                    )
            )
        }
    }
}
