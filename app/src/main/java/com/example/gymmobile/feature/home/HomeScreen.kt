package com.example.gymmobile.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.HistoryRow
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.StatCard
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun HomeScreen(
    onStartWorkout: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Bom treino,",
            style = GymType.body12.copy(color = GymColors.TextMuted),
        )
        Text(
            text = state.greetingName,
            style = GymType.display22,
            modifier = Modifier.padding(top = 2.dp, bottom = 18.dp),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                label = "Sequência",
                value = state.streakValue,
                unit = "dias",
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = "Volume da semana",
                value = state.weekVolume,
                unit = "t",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(20.dp))
        SectionLabel("Treino de hoje")
        TodayCard(
            tag = state.todayTag,
            title = state.todayTitle,
            subtitle = state.todaySubtitle,
            onStart = { onStartWorkout(state.todayTemplateId) },
        )

        Spacer(Modifier.height(22.dp))
        SectionLabel("Últimos treinos")
        state.recent.forEachIndexed { index, workout ->
            HistoryRow(
                left = workout.name,
                right = "${workout.day} · ${workout.duration}",
                showDivider = index != state.recent.lastIndex,
            )
        }
    }
}

/**
 * `.today-card`. O CSS empilha `linear-gradient(155deg, accent-dim, transparent 60%)`
 * sobre `--surface`. 155° em coordenadas de tela (y para baixo) aponta para
 * (0.423, 0.906) — daí o `end.x` proporcional à altura abaixo.
 */
@Composable
private fun TodayCard(
    tag: String,
    title: String,
    subtitle: String,
    onStart: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.cardLarge)
            .drawBehind {
                drawRect(GymColors.Surface)
                drawRect(
                    Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to GymColors.AccentDim,
                            0.6f to Color.Transparent,
                        ),
                        start = Offset.Zero,
                        end = Offset(size.height * 0.466f, size.height),
                    )
                )
            }
            .border(0.5.dp, GymColors.BorderColor, GymShape.cardLarge)
            .padding(16.dp)
    ) {
        Text(text = tag, style = GymType.mono12.copy(color = GymColors.AccentText))
        Text(text = title, style = GymType.display19, modifier = Modifier.padding(top = 4.dp))
        Text(
            text = subtitle,
            style = GymType.body13.copy(color = GymColors.TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp),
        )
        PrimaryButton(text = "Iniciar treino", onClick = onStart, icon = Icons.Filled.PlayArrow)
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun HomeScreenPreview() {
    GymTheme { HomeScreen(onStartWorkout = {}) }
}
