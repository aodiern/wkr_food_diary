package com.example.diary.presentation.combined.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.diary.domain.model.DiaryEntry
import java.time.Duration
import java.time.format.DateTimeFormatter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.diary.presentation.combined.DiaryTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinedListScreen(
    navController: NavController,
    viewModel: CombinedListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val events = viewModel.events
    var editingEntry by remember { mutableStateOf<DiaryEntry?>(null) }


    LaunchedEffect(Unit) {
        events.collect { evt ->
            if (evt is CombinedListViewModel.UiEvent.NavigateToEdit) {
                val route = when (evt.entry) {
                    is DiaryEntry.MealItem       -> "editMeal/${evt.entry.id}"
                    is DiaryEntry.WaterItem      -> "editWater/${evt.entry.id}"
                    is DiaryEntry.SleepItem      -> "editSleep/${evt.entry.id}"
                    is DiaryEntry.StressItem     -> "editStress/${evt.entry.id}"
                    is DiaryEntry.SymptomItem    -> "editSymptom/${evt.entry.id}"
                    is DiaryEntry.WorkoutItem    -> "editWorkout/${evt.entry.id}"
                    is DiaryEntry.MedicationItem -> "editMed/${evt.entry.id}"
                }
                navController.navigate(route)
            }
        }
    }

    Scaffold(
        topBar = { DiaryTopAppBar("Данные за сегодня") }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = uiState.entries,
                        key = { entry -> "${entry.type.name}_${entry.id}" }
                    ) { entry ->
                        EntryRow(
                            entry = entry,
                            onEdit = { editingEntry = entry },
                            onDelete = {
                                viewModel.onEvent(
                                    CombinedListViewModel.CombinedListEvent.DeleteEntry(
                                        entry
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }


        editingEntry?.let { entry ->
            when (entry) {
                is DiaryEntry.MealItem -> EditMealDialog(
                    initialName = entry.mealWithDetails.meal.name,
                    initialMethod = entry.mealWithDetails.method.methodName,
                    onDismiss = { editingEntry = null },
                    onSave = { newName, newMethod ->
                        viewModel.updateMeal(
                            entry.mealWithDetails.copy(
                                meal = entry.mealWithDetails.meal.copy(name = newName),
                                method = entry.mealWithDetails.method.copy(methodName = newMethod)
                            )
                        )
                        editingEntry = null
                    }
                )

                is DiaryEntry.WaterItem -> EditWaterDialog(
                    initial = entry.waterIntake.amountMl,
                    onDismiss = { editingEntry = null },
                    onSave = { newMl ->
                        viewModel.updateWater(entry.waterIntake.copy(amountMl = newMl))
                        editingEntry = null
                    }
                )

                is DiaryEntry.SleepItem -> EditSleepDialog(
                    initialQuality = entry.sleepSession.quality ?: 0,
                    onDismiss = { editingEntry = null },
                    onSave = { newQuality ->
                        viewModel.updateSleep(entry.sleepSession.copy(quality = newQuality))
                        editingEntry = null
                    }
                )

                is DiaryEntry.StressItem -> EditStressDialog(
                    initialLevel = entry.stressLog.level,
                    onDismiss = { editingEntry = null },
                    onSave = { newLevel ->
                        viewModel.updateStress(entry.stressLog.copy(level = newLevel))
                        editingEntry = null
                    }
                )

                is DiaryEntry.SymptomItem -> EditSymptomDialog(
                    initialIntensity = entry.symptom.intensity,
                    onDismiss = { editingEntry = null },
                    onSave = { newVal ->
                        viewModel.updateSymptom(entry.symptom.copy(intensity = newVal))
                        editingEntry = null
                    }
                )

                is DiaryEntry.WorkoutItem -> EditWorkoutDialog(
                    initialIntensity = entry.session.intensity,
                    onDismiss = { editingEntry = null },
                    onSave = { newVal ->
                        viewModel.updateWorkout(entry.session.copy(intensity = newVal))
                        editingEntry = null
                    }
                )

                is DiaryEntry.MedicationItem -> EditMedicationDialog(
                    initialTaken = entry.medLog.taken,
                    onDismiss = { editingEntry = null },
                    onSave = { newTaken ->
                        viewModel.updateMedication(entry.medLog.copy(taken = newTaken))
                        editingEntry = null
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMealDialog(
    initialName: String,
    initialMethod: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var method by remember { mutableStateOf(initialMethod) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать блюдо") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = method,
                    onValueChange = { method = it },
                    label = { Text("Способ приготовления") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, method) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSleepDialog(
    initialQuality: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var quality by remember { mutableStateOf(initialQuality.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Качество сна") },
        text = {
            Column {
                Text("Quality: ${quality.toInt()}")
                Slider(
                    value = quality,
                    onValueChange = { quality = it },
                    valueRange = 0f..10f,
                    steps = 9
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(quality.toInt()) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStressDialog(
    initialLevel: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var level by remember { mutableStateOf(initialLevel.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Уровень стресса") },
        text = {
            Column {
                Text("Level: ${level.toInt()}")
                Slider(
                    value = level,
                    onValueChange = { level = it },
                    valueRange = 0f..10f,
                    steps = 9
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(level.toInt()) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutDialog(
    initialIntensity: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var intensity by remember { mutableStateOf(initialIntensity.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Интенсивность тренировки") },
        text = {
            Column {
                Text("Intensity: ${intensity.toInt()}")
                Slider(
                    value = intensity,
                    onValueChange = { intensity = it },
                    valueRange = 0f..10f,
                    steps = 9
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(intensity.toInt()) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationDialog(
    initialTaken: Boolean,
    onDismiss: () -> Unit,
    onSave: (Boolean) -> Unit
) {
    var taken by remember { mutableStateOf(initialTaken) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Принятие лекарства") },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = taken,
                    onCheckedChange = { taken = it }
                )
                Spacer(Modifier.width(8.dp))
                Text("Принято")
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(taken) }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWaterDialog(
    initial: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var text by remember { mutableStateOf(initial.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Water Intake") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.filter(Char::isDigit) },
                label = { Text("Volume (ml)") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val ml = text.toIntOrNull() ?: initial
                onSave(ml)
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSymptomDialog(
    initialIntensity: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var sliderPos by remember { mutableStateOf(initialIntensity.toFloat()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Symptom Intensity") },
        text = {
            Column {
                Text("Intensity: ${'$'}{sliderPos.toInt()}")
                Slider(
                    value = sliderPos,
                    onValueChange = { sliderPos = it },
                    valueRange = 0f..10f,
                    steps = 9
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(sliderPos.toInt()) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryRow(
    entry: DiaryEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {

            Text(
                text = entry.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(4.dp))


            Text(entry.title, style = MaterialTheme.typography.titleMedium)
            entry.subtitle?.let {
                Spacer(Modifier.height(2.dp))
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))


//            when (entry) {
//                is DiaryEntry.MealItem -> {
//                    Text("Ингредиенты:", style = MaterialTheme.typography.labelSmall)
//                    entry.mealWithDetails.ingredients.forEach { ing ->
//                        Text("• ${ing.name}", style = MaterialTheme.typography.bodySmall)
//                    }
//                }
//                is DiaryEntry.WaterItem -> Text(
//                    "Объем: ${entry.waterIntake.amountMl} мл",
//                    style = MaterialTheme.typography.bodySmall
//                )
//                is DiaryEntry.SleepItem -> {
//                    val dur = Duration.between(entry.sleepSession.startTime, entry.sleepSession.endTime)
//                    val h = dur.toHours()
//                    val m = dur.minusHours(h).toMinutes()
//                    Text("Продолжительность: ${h}h ${m}m", style = MaterialTheme.typography.bodySmall)
//                    entry.sleepSession.quality?.let { q ->
//                        Text("Качество: $q/10", style = MaterialTheme.typography.bodySmall)
//                    }
//                }
//                is DiaryEntry.StressItem -> Text(
//                    "Уровень стресса: ${entry.stressLog.level}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//                is DiaryEntry.SymptomItem -> {
//                    Text("Интенсивность: ${entry.symptom.intensity}", style = MaterialTheme.typography.bodySmall)
//                }
//                is DiaryEntry.WorkoutItem -> {
//                    Text("Интенсивность: ${entry.session.intensity}", style = MaterialTheme.typography.bodySmall)
//                }
//                is DiaryEntry.MedicationItem -> {
//                    Text(
//                        "Дозировка: ${entry.medLog.dose}, Принято: ${if (entry.medLog.taken) "✓" else "✗"}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//            if (entry is DiaryEntry.MealItem) {
//                Text("Ингредиенты:", style = MaterialTheme.typography.labelSmall)
//                entry.mealWithDetails.ingredients.forEach { ing ->
//                    Text("• ${ing.name}", style = MaterialTheme.typography.bodySmall)
//                }
//                Spacer(Modifier.height(4.dp))
//                Text("Способ приготовления:", style = MaterialTheme.typography.labelSmall)
//                Text(entry.mealWithDetails.method.methodName, style = MaterialTheme.typography.bodySmall)
//                Spacer(Modifier.height(8.dp))
//            }

            Spacer(Modifier.height(8.dp))


            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}