package com.example.gymmobile.feature.active

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.ActiveExercise
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ActiveWorkoutScreen(
    templateId: String,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    viewModel: ActiveWorkoutViewModel = viewModel(
        factory = ActiveWorkoutViewModel.factory(templateId)
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(GymColors.Bg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
        ) {
            // `.workout-topbar`
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SquareIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    onClick = onBack,
                )
                Text(
                    text = state.elapsedLabel,
                    style = GymType.mono13.copy(color = GymColors.AccentText),
                )
                SquareIconButton(
                    icon = Icons.Filled.Close,
                    contentDescription = "Encerrar treino",
                    onClick = onFinish,
                )
            }

            SectionLabel(state.contextLabel)

            state.exercises.forEachIndexed { exerciseIndex, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onWeightChange = { setIndex, raw ->
                        viewModel.onWeightChange(exerciseIndex, setIndex, raw)
                    },
                    onRepsChange = { setIndex, raw ->
                        viewModel.onRepsChange(exerciseIndex, setIndex, raw)
                    },
                    onToggleDone = { setIndex -> viewModel.toggleSetDone(exerciseIndex, setIndex) },
                    onAddSet = { viewModel.addSet(exerciseIndex) },
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(8.dp))
            PrimaryButton(text = "Finalizar treino", onClick = onFinish)
        }

        state.rest?.let { rest ->
            RestOverlay(
                remaining = rest.remaining,
                progress = rest.progress,
                onAdjust = viewModel::adjustRest,
                onSkip = viewModel::skipRest,
            )
        }
    }
}

/** `.exercise-card`. */
@Composable
private fun ExerciseCard(
    exercise: ActiveExercise,
    onWeightChange: (Int, String) -> Unit,
    onRepsChange: (Int, String) -> Unit,
    onToggleDone: (Int) -> Unit,
    onAddSet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.card)
            .background(GymColors.Surface)
            .border(0.5.dp, GymColors.BorderColor, GymShape.card)
            .padding(14.dp)
    ) {
        Text(
            text = exercise.name,
            style = GymType.display16,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        // `.set-header` — colunas 26 | 1fr | 1fr | 32
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeaderCell("Série", Modifier.width(26.dp))
            HeaderCell("Kg", Modifier.weight(1f))
            HeaderCell("Reps", Modifier.weight(1f))
            Spacer(Modifier.width(32.dp))
        }

        exercise.sets.forEachIndexed { setIndex, set ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${setIndex + 1}",
                    style = GymType.mono12.copy(color = GymColors.TextSecondary),
                    modifier = Modifier.width(26.dp),
                )
                SetNumberField(
                    fieldKey = "${exercise.name}-$setIndex-kg",
                    initial = ActiveWorkoutViewModel.formatWeight(set.weight),
                    onValueChange = { raw -> onWeightChange(setIndex, raw) },
                    modifier = Modifier.weight(1f),
                )
                SetNumberField(
                    fieldKey = "${exercise.name}-$setIndex-reps",
                    initial = set.reps.toString(),
                    onValueChange = { raw -> onRepsChange(setIndex, raw) },
                    modifier = Modifier.weight(1f),
                )
                CheckToggle(done = set.done, onClick = { onToggleDone(setIndex) })
            }
        }

        Text(
            text = "+ Adicionar série",
            style = GymType.body12.copy(color = GymColors.TextSecondary),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAddSet)
                .padding(vertical = 6.dp),
        )
    }
}

@Composable
private fun HeaderCell(text: String, modifier: Modifier) {
    Text(
        text = text.uppercase(),
        style = GymType.mono10.copy(color = GymColors.TextMuted),
        modifier = modifier,
    )
}

/**
 * `.set-row input`. O texto digitado é local para que o campo possa ficar
 * vazio enquanto o usuário apaga; o ViewModel recebe o valor já parseado.
 */
@Composable
private fun SetNumberField(
    fieldKey: String,
    initial: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember(fieldKey) { mutableStateOf(initial) }
    var focused by remember(fieldKey) { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = { raw ->
            text = raw
            onValueChange(raw)
        },
        singleLine = true,
        textStyle = GymType.mono13.copy(textAlign = TextAlign.Center),
        cursorBrush = SolidColor(GymColors.Accent),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .height(32.dp)
            .clip(GymShape.input)
            .background(GymColors.Surface2)
            .border(
                width = 0.5.dp,
                color = if (focused) GymColors.Accent else GymColors.BorderColor,
                shape = GymShape.input,
            )
            .onFocusChanged { focused = it.isFocused },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { innerTextField() }
        },
    )
}

/** `.check-toggle` / `.check-toggle.done`. */
@Composable
private fun CheckToggle(done: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(GymShape.input)
            .background(if (done) GymColors.SuccessDim else GymColors.Surface2)
            .border(
                width = 0.5.dp,
                color = if (done) GymColors.Success else GymColors.BorderColor,
                shape = GymShape.input,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = if (done) "Desmarcar série" else "Concluir série",
            tint = if (done) GymColors.Success else GymColors.TextMuted,
            modifier = Modifier.size(15.dp),
        )
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ActiveWorkoutScreenPreview() {
    GymTheme { ActiveWorkoutScreen(templateId = "push-a", onBack = {}, onFinish = {}) }
}
