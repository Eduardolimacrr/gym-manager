package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.BodyMeasure
import com.example.gymmobile.data.WeightEntry
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

@Composable
fun BodyMetricsScreen(
    onBack: () -> Unit,
    viewModel: BodyMetricsViewModel = viewModel()
) {
    val weightHistory by viewModel.weightHistory.collectAsStateWithLifecycle()
    val measures by viewModel.measures.collectAsStateWithLifecycle()
    val bodyFat by viewModel.bodyFat.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.padding(bottom = 12.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = GymColors.TextPrimary
            )
        }

        Text(
            text = "Medidas Corporais",
            style = GymType.display24,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        SectionLabel("Evolução do Peso")
        WeightChartCard(weightHistory)

        Spacer(modifier = Modifier.height(24.dp))

        SectionLabel("Composição")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InfoBox(label = "Peso atual", value = "${weightHistory.last().weight} kg", modifier = Modifier.weight(1f))
            InfoBox(label = "Gordura", value = bodyFat, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionLabel("Medidas Detalhadas")
        measures.forEachIndexed { index, measure ->
            MeasureRow(measure, showDivider = index != measures.lastIndex)
        }
    }
}

@Composable
private fun WeightChartCard(history: List<WeightEntry>) {
    val minWeight = history.minOfOrNull { it.weight } ?: 0.0
    val maxWeight = history.maxOfOrNull { it.weight } ?: 100.0
    val weightRange = (maxWeight - minWeight).coerceAtLeast(2.0) // Pelo menos 2kg de range visual

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.cardLarge)
            .background(GymColors.Surface)
            .border(0.5.dp, GymColors.BorderColor, GymShape.cardLarge)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // Aumentado para melhor visualização
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            history.forEach { entry ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${entry.weight}",
                        style = GymType.mono10.copy(color = GymColors.TextSecondary, fontSize = 9.sp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    // Normaliza o peso no gráfico: o menor peso fica com altura mínima
                    val normalizedHeight = ((entry.weight - (minWeight - 0.5)) / (weightRange + 1.0)) * 80
                    
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(normalizedHeight.coerceAtLeast(8.0).dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(GymColors.AccentDim)
                            .border(
                                1.dp, 
                                GymColors.Accent.copy(alpha = 0.4f), 
                                androidx.compose.foundation.shape.RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = entry.date, style = GymType.mono10.copy(color = GymColors.TextMuted))
                }
            }
        }
    }
}

@Composable
private fun InfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(GymShape.cardLarge)
            .background(GymColors.Surface)
            .border(0.5.dp, GymColors.BorderColor, GymShape.cardLarge)
            .padding(16.dp)
    ) {
        Text(text = label, style = GymType.mono11.copy(color = GymColors.TextSecondary))
        Text(text = value, style = GymType.display19, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun MeasureRow(measure: BodyMeasure, showDivider: Boolean) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = measure.name, style = GymType.body14.copy(fontWeight = FontWeight.Medium))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = measure.value, style = GymType.mono13)
                measure.change?.let {
                    Text(
                        text = " ($it)",
                        style = GymType.mono11.copy(
                            color = if (it.startsWith("+")) GymColors.AccentText else GymColors.Success
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        }
    }
}
