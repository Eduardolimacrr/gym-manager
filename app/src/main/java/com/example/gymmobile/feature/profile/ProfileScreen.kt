package com.example.gymmobile.feature.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.SettingsRow
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProfileScreen() {
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(GymShape.card)
                .background(GymColors.Surface)
                .border(0.5.dp, GymColors.BorderColor, GymShape.card)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(GymColors.Surface2),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "MS",
                    style = GymType.display20.copy(color = GymColors.AccentText),
                )
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(text = "Marcos Silva", style = GymType.display18)
                Text(
                    text = "Conta ativa",
                    style = GymType.body13.copy(color = GymColors.Success),
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    text = "Plano iniciante",
                    style = GymType.mono11.copy(color = GymColors.TextMuted),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        SectionLabel("Preferencias")
        SettingsRow(
            title = "Unidade de peso",
            subtitle = "Quilos",
        ) {
            Text(text = "kg", style = GymType.mono12.copy(color = GymColors.TextSecondary))
        }
        SettingsRow(
            title = "Notificacoes de descanso",
            subtitle = "Ativadas",
        ) {
            Text(text = "On", style = GymType.mono12.copy(color = GymColors.AccentText))
        }
        SettingsRow(
            title = "Lembrete de treino",
            subtitle = "Dias uteis as 18:00",
            showDivider = false,
        ) {
            Text(text = "18:00", style = GymType.mono12.copy(color = GymColors.TextSecondary))
        }

        Spacer(Modifier.height(22.dp))

        SectionLabel("Dados")
        SettingsRow(
            title = "Backup automatico",
            subtitle = "Semanal",
            showDivider = false,
        ) {
            Text(text = "Ativo", style = GymType.mono12.copy(color = GymColors.Success))
        }

        Spacer(Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(GymShape.button)
                .background(Color(0xFFE04A3A))
                .clickable { }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Sair da Conta",
                style = GymType.body14.copy(
                    color = GymColors.TextPrimary,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProfileScreenPreview() {
    GymTheme { ProfileScreen() }
}
