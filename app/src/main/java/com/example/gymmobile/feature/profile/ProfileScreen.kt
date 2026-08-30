package com.example.gymmobile.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.ui.components.GhostButton
import com.example.gymmobile.ui.components.SettingsRow
import com.example.gymmobile.ui.components.ToggleSwitch
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Perfil",
            style = GymType.display19,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
        )

        // `.profile-header`
        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(GymColors.AccentDim),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = FakeRepository.userInitials,
                    style = GymType.display20.copy(color = GymColors.AccentText),
                )
            }
            Column {
                Text(text = FakeRepository.userFullName, style = GymType.display18)
                Text(
                    text = FakeRepository.userSubtitle,
                    style = GymType.body12.copy(color = GymColors.TextSecondary),
                )
            }
        }

        SettingsRow(title = "Unidade de peso") {
            UnitToggle(selected = state.unit, onSelect = viewModel::setUnit)
        }
        SettingsRow(
            title = "Notificações de descanso",
            subtitle = "Avisar quando o tempo de descanso acabar",
        ) {
            ToggleSwitch(
                checked = state.restNotifications,
                onCheckedChange = viewModel::setRestNotifications,
            )
        }
        SettingsRow(
            title = "Lembrete de treino",
            subtitle = "Notificação nos dias programados",
        ) {
            ToggleSwitch(
                checked = state.workoutReminder,
                onCheckedChange = viewModel::setWorkoutReminder,
            )
        }
        SettingsRow(title = "Backup automático") {
            ToggleSwitch(
                checked = state.autoBackup,
                onCheckedChange = viewModel::setAutoBackup,
            )
        }

        Spacer(Modifier.height(22.dp))
        GhostButton(text = "Sair da conta", onClick = { /* sem ação: fora de escopo */ })
    }
}

/**
 * `.unit-toggle`. O CSS pinta o item ativo com `var(--surface-1,#31353f)` —
 * `--surface-1` nunca é definido, então vale o fallback, que é exatamente
 * `--border`. Daí `GymColors.BorderColor` aqui.
 */
@Composable
private fun UnitToggle(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clip(GymShape.iconButton)
            .background(GymColors.Surface2)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        listOf("kg", "lb").forEach { unit ->
            val active = unit == selected
            Text(
                text = unit,
                style = GymType.mono12.copy(
                    color = if (active) GymColors.TextPrimary else GymColors.TextMuted,
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(if (active) GymColors.BorderColor else Color.Transparent)
                    .clickable { onSelect(unit) }
                    .padding(horizontal = 12.dp, vertical = 5.dp),
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProfileScreenPreview() {
    GymTheme { ProfileScreen() }
}
