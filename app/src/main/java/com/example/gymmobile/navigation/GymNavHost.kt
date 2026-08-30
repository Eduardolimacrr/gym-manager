package com.example.gymmobile.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymmobile.feature.active.ActiveWorkoutScreen
import com.example.gymmobile.feature.create.CreateWorkoutScreen
import com.example.gymmobile.feature.home.HomeScreen
import com.example.gymmobile.feature.profile.ProfileScreen
import com.example.gymmobile.feature.progress.ProgressDetailScreen
import com.example.gymmobile.feature.progress.ProgressScreen
import com.example.gymmobile.feature.workouts.WorkoutsScreen
import com.example.gymmobile.ui.theme.GymColors

@Composable
fun GymApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        containerColor = GymColors.Bg,
        bottomBar = {
            // `.bottom-nav.hidden`: some fora das 4 abas.
            if (currentRoute in Routes.TAB_ROUTES) {
                GymBottomBar(currentRoute) { navController.navigateToTab(it) }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onStartWorkout = { id -> navController.navigate(Routes.active(id)) },
                )
            }
            composable(Routes.WORKOUTS) {
                WorkoutsScreen(
                    onOpenTemplate = { id -> navController.navigate(Routes.active(id)) },
                    onEditTemplate = { navController.navigate(Routes.CREATE) },
                    onCreate = { navController.navigate(Routes.CREATE) },
                )
            }
            composable(Routes.PROGRESS) {
                ProgressScreen(
                    onOpenExercise = { id -> navController.navigate(Routes.progressDetail(id)) },
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen()
            }
            composable(Routes.CREATE) {
                CreateWorkoutScreen(
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        navController.navigate(Routes.WORKOUTS) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(
                route = Routes.ACTIVE,
                arguments = listOf(navArgument(Routes.ARG_TEMPLATE_ID) { type = NavType.StringType }),
            ) { entry ->
                ActiveWorkoutScreen(
                    templateId = entry.arguments?.getString(Routes.ARG_TEMPLATE_ID).orEmpty(),
                    onBack = { navController.popBackStack() },
                    onFinish = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                route = Routes.PROGRESS_DETAIL,
                arguments = listOf(navArgument(Routes.ARG_EXERCISE_ID) { type = NavType.StringType }),
            ) { entry ->
                ProgressDetailScreen(
                    exerciseId = entry.arguments?.getString(Routes.ARG_EXERCISE_ID).orEmpty(),
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

/** Troca de aba sem empilhar destinos, preservando o estado de cada aba. */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
