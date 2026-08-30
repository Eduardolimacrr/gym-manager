package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.HistoryRow
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.components.StatCard
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProgressDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ProgressDetailViewModel = viewModel(
        factory = ProgressDetailViewModel.factory(exerciseId)
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SquareIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                onClick = onBack,
            )
            Text(text = state.name, style = GymType.display19)
        }

        // `.detail-hero`
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = state.pr, style = GymType.display44.copy(color = GymColors.Chalk))
            Spacer(Modifier.width(10.dp))
            Text(
                text = "recorde pessoal",
                style = GymType.body13.copy(color = GymColors.TextSecondary),
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }

        Spacer(Modifier.height(20.dp))
        BarChart(bars = state.bars)
        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(label = "Sessões", value = state.sessions, modifier = Modifier.weight(1f))
            StatCard(label = "Última carga", value = state.lastLoad, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))
        SectionLabel("Histórico")
        state.history.forEachIndexed { index, entry ->
            HistoryRow(
                left = entry.date,
                right = entry.value,
                showDivider = index != state.history.lastIndex,
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProgressDetailScreenPreview() {
    GymTheme { ProgressDetailScreen(exerciseId = "supino-reto", onBack = {}) }
}
