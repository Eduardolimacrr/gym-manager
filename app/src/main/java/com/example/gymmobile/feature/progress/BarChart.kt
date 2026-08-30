package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/**
 * `.chart-bars` — contêiner de 130dp, alturas em dp iguais aos px do CSS
 * (máximo 100), última coluna em `Accent` (`.bar.peak`).
 */
@Composable
fun BarChart(bars: List<Int>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().height(130.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        bars.forEachIndexed { index, value ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(value.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(
                            if (index == bars.lastIndex) GymColors.Accent
                            else GymColors.Surface2
                        )
                )
                Text(
                    text = "${index + 1}",
                    style = GymType.mono10.copy(color = GymColors.TextMuted),
                )
            }
        }
    }
}
