package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/** `.stat-card` — rótulo pequeno acima, número grande abaixo com unidade opcional. */
@Composable
fun StatCard(
    label: String,
    value: String,
    unit: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(GymShape.card)
            .background(GymColors.Surface)
            .padding(14.dp)
    ) {
        Text(text = label, style = GymType.body11.copy(color = GymColors.TextMuted))
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, style = GymType.display24)
            if (unit != null) {
                Spacer(Modifier.width(5.dp))
                Text(
                    text = unit,
                    style = GymType.body13.copy(color = GymColors.TextSecondary),
                    modifier = Modifier.padding(bottom = 3.dp),
                )
            }
        }
    }
}
