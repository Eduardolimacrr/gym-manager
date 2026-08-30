package com.example.gymmobile.feature.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.components.SelectableChip
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateWorkoutScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: CreateWorkoutViewModel = viewModel(),
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
            Text(text = "Novo treino", style = GymType.display19)
        }

        FieldLabel("Nome do treino")
        NameField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
        )

        Spacer(Modifier.height(18.dp))
        FieldLabel("Adicionar exercícios")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.catalog.forEach { name ->
                SelectableChip(
                    text = name,
                    selected = name in state.picked,
                    onClick = { viewModel.toggleExercise(name) },
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        FieldLabel("Selecionados")
        if (state.picked.isEmpty()) {
            Text(
                text = "Nenhum exercício selecionado ainda.",
                style = GymType.body13.copy(color = GymColors.TextMuted),
            )
        } else {
            state.picked.forEach { name ->
                PickedRow(name = name, onRemove = { viewModel.toggleExercise(name) })
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        PrimaryButton(
            text = "Salvar treino",
            onClick = {
                viewModel.save()
                onSaved()
            },
        )
    }
}

/** `.field-label`. */
@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = GymType.sectionLabel,
        modifier = Modifier.padding(bottom = 6.dp),
    )
}

/** `.text-input` — borda vira laranja com o foco. */
@Composable
private fun NameField(value: String, onValueChange: (String) -> Unit) {
    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = GymType.body15,
        cursorBrush = SolidColor(GymColors.Accent),
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(GymShape.button)
            .background(GymColors.Surface)
            .border(
                width = 0.5.dp,
                color = if (focused) GymColors.Accent else GymColors.BorderColor,
                shape = GymShape.button,
            )
            .onFocusChanged { focused = it.isFocused }
            .padding(horizontal = 14.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "Ex: Push A, Upper, Dia de perna",
                        style = GymType.body15.copy(color = GymColors.TextMuted),
                    )
                }
                innerTextField()
            }
        },
    )
}

/** `.picked-row`. */
@Composable
private fun PickedRow(name: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.iconButton)
            .background(GymColors.Surface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = name, style = GymType.body13)
        Text(
            text = "remover",
            style = GymType.body13.copy(color = GymColors.TextMuted),
            modifier = Modifier.clickable(onClick = onRemove),
        )
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun CreateWorkoutScreenPreview() {
    GymTheme { CreateWorkoutScreen(onBack = {}, onSaved = {}) }
}
