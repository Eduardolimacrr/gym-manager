package com.example.gymmobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/** `.history-row` — texto à esquerda, valor mono à direita, divisor abaixo. */
@Composable
fun HistoryRow(left: String, right: String, showDivider: Boolean = true) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = left, style = GymType.body13)
            Text(text = right, style = GymType.mono12.copy(color = GymColors.TextMuted))
        }
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        }
    }
}
