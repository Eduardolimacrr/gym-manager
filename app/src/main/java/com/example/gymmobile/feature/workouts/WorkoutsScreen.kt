package com.example.gymmobile.feature.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.WorkoutTemplate
import com.example.gymmobile.ui.components.GhostButton
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun WorkoutsScreen(
    onOpenTemplate: (String) -> Unit,
    onEditTemplate: () -> Unit,
    onCreate: () -> Unit,
    viewModel: WorkoutsViewModel = viewModel(),
) {
    val templates by viewModel.templates.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 18.dp),
    ) {
        item {
            Text(
                text = "Meus treinos",
                style = GymType.display19,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
            )
        }
        items(items = templates, key = { it.id }) { template ->
            TemplateCard(
                template = template,
                onEdit = onEditTemplate,
                onPlay = { onOpenTemplate(template.id) },
            )
            Spacer(Modifier.height(10.dp))
        }
        item {
            Spacer(Modifier.height(6.dp))
            GhostButton(text = "Novo treino", onClick = onCreate, icon = Icons.Filled.Add)
        }
    }
}

/** `.template-card`. */
@Composable
private fun TemplateCard(
    template: WorkoutTemplate,
    onEdit: () -> Unit,
    onPlay: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.card)
            .background(GymColors.Surface)
            .border(0.5.dp, GymColors.BorderColor, GymShape.card)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
            Text(text = template.name, style = GymType.display16)
            Text(
                text = template.tag,
                style = GymType.body12.copy(color = GymColors.TextSecondary),
                modifier = Modifier.padding(top = 2.dp),
            )
            Text(
                text = "${template.exerciseCount} exercícios",
                style = GymType.mono11.copy(color = GymColors.TextMuted),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SquareIconButton(
                icon = Icons.Outlined.Edit,
                contentDescription = "Editar ${template.name}",
                onClick = onEdit,
                size = 32.dp,
                shape = GymShape.miniButton,
                container = GymColors.Surface2,
            )
            SquareIconButton(
                icon = Icons.Filled.PlayArrow,
                contentDescription = "Iniciar ${template.name}",
                onClick = onPlay,
                accent = true,
                size = 32.dp,
                shape = GymShape.miniButton,
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun WorkoutsScreenPreview() {
    GymTheme {
        WorkoutsScreen(onOpenTemplate = {}, onEditTemplate = {}, onCreate = {})
    }
}
