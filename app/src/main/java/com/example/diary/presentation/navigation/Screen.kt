package com.example.diary.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object MealList : Screen(
        route = "meal_list",
        title = "Приёмы",
        icon = Icons.AutoMirrored.Filled.List
    )
    object Statistics : Screen(
        route = "statistics",
        title = "Статистика",
        icon = Icons.Default.MoreHoriz
    )
    object AddMeal : Screen(
        route = "add_meal",
        title = "Добавить",
        icon = Icons.Default.Add
    )
    object Calendar : Screen(
        route = "calendar",
        title = "Календарь",
        icon = Icons.Default.CalendarToday
    )
    object Settings : Screen(
        route = "settings",
        title = "Настройки",
        icon = Icons.Default.Settings
    )
}
