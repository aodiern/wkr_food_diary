package com.example.diary.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.diary.presentation.combined.profile.LockScreen
import com.example.diary.presentation.combined.profile.ProfileScreen
import com.example.diary.presentation.combined.profile.ProfileViewModel
import com.example.diary.presentation.meal.screen.AddMealScreenWrapper
import com.example.diary.presentation.medication.screen.MedicationDefinitionScreen
import com.example.diary.presentation.medication.screen.MedicationTrackerScreen
import com.example.diary.presentation.medication.viewmodel.MedicationDefinitionViewModel
import com.example.diary.presentation.notification.NotificationListScreen
import com.example.diary.presentation.sleep.SleepTrackerScreen
import com.example.diary.presentation.stress.AddStressScreen
import com.example.diary.presentation.symptom.screen.AddSymptomWithTypeScreenWrapper
import com.example.diary.presentation.water.WaterIntakeScreen
import com.example.diary.presentation.workout.AddWorkoutScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.combined.calendar.CombinedCalendarScreen
import com.example.diary.presentation.combined.list.CombinedListScreen
import com.example.diary.presentation.combined.stats.StatsScreen
import com.example.diary.presentation.medication.screen.MedicationPlanScreen

sealed class NavRoutes(val route: String, val title: String) {
    object CombinedList : NavRoutes("combined_list", "Дневник")
    object AddMeal      : NavRoutes("add_meal", "Добавить приём пищи")
    object AddSymptom   : NavRoutes("add_symptom", "Добавить симптомы")
    object AddStress    : NavRoutes("add_stress", "Добавить стресс")
    object AddWorkout   : NavRoutes("add_workout", "Добавить тренировку")
    object AddDream    : NavRoutes("add_dream", "Добавить часы сна")
    object Statistics   : NavRoutes("statistics", "Статистика")
    object Calendar     : NavRoutes("calendar", "Календарь")
    object Settings     : NavRoutes("settings", "Настройки")
    object Notifications: NavRoutes("notifications", "Уведомления")
    object MedDefinitions: NavRoutes("med/definitions", "Справочник лекарств")
    object MedPlans      : NavRoutes("med/plans", "Планы лекарств")
    object MedTracker    : NavRoutes("med/tracker", "Трекер лекарств")
    object Lock          : NavRoutes("lock", "Блокировка")
    object Drinks        : NavRoutes("drinks", "Напитки")
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val profile by profileViewModel.profileState.collectAsState()
    val startRoute = if (profile?.passwordHash.isNullOrEmpty()) NavRoutes.CombinedList.route else NavRoutes.Lock.route

    var showMenu by remember { mutableStateOf(false) }

    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route
    val showBottomBar = currentRoute != NavRoutes.Lock.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar {
                    val items = listOf(
                        NavRoutes.CombinedList,
                        NavRoutes.Statistics,
                        NavRoutes.AddMeal,
                        NavRoutes.Calendar,
                        NavRoutes.Settings
                    )
                    items.forEach { screen ->
                        if (screen is NavRoutes.AddMeal) {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Add, contentDescription = screen.title) },
                                selected = false,
                                onClick = { showMenu = !showMenu },
                                alwaysShowLabel = false
                            )
                        } else {
                            val icon = when (screen) {
                                NavRoutes.CombinedList -> Icons.AutoMirrored.Filled.List
                                NavRoutes.Statistics   -> Icons.Default.BarChart
                                NavRoutes.Calendar     -> Icons.Default.CalendarToday
                                NavRoutes.Settings     -> Icons.Default.Settings
                                else                   -> Icons.Default.List
                            }
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = screen.title) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    showMenu = false
                                    if (screen == NavRoutes.Settings) navController.popBackStack()
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                alwaysShowLabel = false
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = startRoute
            ) {
                composable(NavRoutes.Lock.route) {
                    LockScreen(onUnlock = {
                        navController.navigate(NavRoutes.CombinedList.route) {
                            popUpTo(NavRoutes.Lock.route) { inclusive = true }
                        }
                    })
                }
                composable(NavRoutes.CombinedList.route) {
                    CombinedListScreen(navController)
                }
                composable(NavRoutes.Drinks.route) {
                    WaterIntakeScreen()
                }
                composable(NavRoutes.Notifications.route) {
                    NotificationListScreen()
                }
                composable(NavRoutes.AddMeal.route) {
                    AddMealScreenWrapper(onSaved = { navController.popBackStack() })
                }
                composable(NavRoutes.AddSymptom.route) {
                    AddSymptomWithTypeScreenWrapper(navController)
                }
                composable(NavRoutes.Statistics.route) {
                    StatsScreen()
                }
                composable(NavRoutes.Calendar.route) {
                    CombinedCalendarScreen()
                }
                composable(NavRoutes.Settings.route) {
                    ProfileScreen(navController = navController)
                }
                composable(NavRoutes.AddStress.route) {
                    AddStressScreen(onBack = navController::popBackStack)
                }
                composable(NavRoutes.AddDream.route) {
                    SleepTrackerScreen(onBack = navController::popBackStack)
                }
                composable(NavRoutes.AddWorkout.route) {
                    AddWorkoutScreen(navController = navController)
                }
                composable(NavRoutes.MedDefinitions.route) {
                    MedicationDefinitionScreen()
                }
                composable(NavRoutes.MedPlans.route) {
                    val defs by hiltViewModel<MedicationDefinitionViewModel>().definitions.collectAsState()
                    MedicationPlanScreen(defs = defs)
                }
                composable(NavRoutes.MedTracker.route) {
                    MedicationTrackerScreen()
                }
            }

            if (showMenu && showBottomBar) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { showMenu = false }
                )
                Column(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.AddMeal.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить приём пищи") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.AddSymptom.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить симптомы") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.MedDefinitions.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить лекарства") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.MedPlans.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить план приема лекарств") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.MedTracker.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Трекер лекарств") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.Drinks.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить напиток") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.AddStress.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить уровень стресса") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.AddWorkout.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить тренировку") }
                    Button(onClick = {
                        showMenu = false
                        navController.navigate(NavRoutes.AddDream.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Добавить часы сна") }
                }
            }
        }
    }
}
