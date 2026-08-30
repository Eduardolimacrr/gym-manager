package com.example.gymmobile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymType

/**
 * `.section-label`. O `text-transform:uppercase` do CSS é aplicado aqui, no
 * conteúdo — Compose não tem equivalente de estilo.
 */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = GymType.sectionLabel,
        modifier = modifier.padding(top = 4.dp, bottom = 10.dp),
    )
}
