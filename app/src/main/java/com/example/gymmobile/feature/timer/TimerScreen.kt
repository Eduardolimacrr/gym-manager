package com.example.gymmobile.feature.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.GhostButton
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

@Composable
fun TimerScreen(
    onBack: () -> Unit,
    viewModel: TimerViewModel = viewModel()
) {
    val timeInMillis by viewModel.elapsedMillis.collectAsStateWithLifecycle()
    val isRunning by viewModel.isRunning.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
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
            text = "Cronômetro",
            style = GymType.display24,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = viewModel.formatTime(timeInMillis),
                style = GymType.display44.copy(fontSize = 72.sp, lineHeight = 72.sp),
                color = GymColors.TextPrimary
            )
        }

        Row(
            modifier = Modifier.padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                PrimaryButton(
                    text = if (isRunning) "Pausar" else "Iniciar",
                    onClick = { viewModel.toggleTimer() },
                    icon = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow
                )
            }
            
            IconButton(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier
                    .size(52.dp)
                    .background(GymColors.Surface2, androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Resetar",
                    tint = GymColors.TextSecondary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}
