package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.ProgressExercise
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProgressScreen(
    onOpenExercise: (String) -> Unit,
    onOpenMetrics: () -> Unit,
    viewModel: ProgressViewModel = viewModel(),
) {
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Progresso",
            style = GymType.display19,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
        )

        // Botão para Medidas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GymColors.Surface, GymShape.cardLarge)
                .border(0.5.dp, GymColors.BorderColor, GymShape.cardLarge)
                .clickable(onClick = onOpenMetrics)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Straighten, contentDescription = null, tint = GymColors.Accent)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Medidas Corporais", style = GymType.body14.copy(fontWeight = FontWeight.Medium))
                Text(text = "Peso, gordura e medidas", style = GymType.body12.copy(color = GymColors.TextSecondary))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GymColors.TextMuted)
        }

        Spacer(modifier = Modifier.padding(vertical = 12.dp))
        exercises.forEachIndexed { index, exercise ->
            ExerciseListRow(
                exercise = exercise,
                showDivider = index != exercises.lastIndex,
                onClick = { onOpenExercise(exercise.id) },
            )
        }
    }
}

/** `.exercise-list-row`. */
@Composable
private fun ExerciseListRow(
    exercise: ProgressExercise,
    showDivider: Boolean,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Sparkline(bars = exercise.bars)
            Text(
                text = exercise.name,
                style = GymType.body14.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = exercise.pr,
                style = GymType.mono12.copy(color = GymColors.TextSecondary),
            )
        }
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProgressScreenPreview() {
    GymTheme { ProgressScreen(onOpenExercise = {}, onOpenMetrics = {}) }
}
