package com.example.gymmobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/** `.bottom-nav`. Renderizada apenas nas rotas-aba — ver `GymApp`. */
@Composable
fun GymBottomBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.background(GymColors.Bg)) {
        HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 22.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            NavItem(Icons.Outlined.Home, "Início", Routes.HOME, currentRoute, onNavigate)
            NavItem(Icons.Outlined.FitnessCenter, "Treinos", Routes.WORKOUTS, currentRoute, onNavigate)
            NavItem(Icons.Outlined.ShowChart, "Progresso", Routes.PROGRESS, currentRoute, onNavigate)
            NavItem(Icons.Outlined.Person, "Perfil", Routes.PROFILE, currentRoute, onNavigate)
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    route: String,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    val selected = currentRoute == route
    val color = if (selected) GymColors.AccentText else GymColors.TextMuted
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.clickable { onNavigate(route) }.padding(horizontal = 8.dp),
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(21.dp))
        Text(text = label, style = GymType.body11.copy(color = color))
    }
}
