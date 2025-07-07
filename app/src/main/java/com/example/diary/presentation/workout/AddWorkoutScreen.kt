package com.example.diary.presentation.workout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.diary.presentation.workout.WorkoutViewModel
import com.example.diary.domain.model.workout.WorkoutSession
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onBack: () -> Unit = { navController.popBackStack() }
) {
    val context = LocalContext.current
    val types by viewModel.types.collectAsState()
    var selectedTypeId by remember { mutableStateOf<Long?>(null) }
    var startTime by remember { mutableStateOf(LocalDateTime.now()) }
    var endTime by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    var intensity by remember { mutableStateOf(5f) }
    var newTypeName by remember { mutableStateOf("") }
    var typeError by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun createDateTimePicker(initial: LocalDateTime, onUpdate: (LocalDateTime) -> Unit): () -> Unit {
        return {
            val timePicker = TimePickerDialog(
                context,
                { _: TimePicker, h, m -> onUpdate(initial.withHour(h).withMinute(m)) },
                initial.hour, initial.minute, true
            )
            DatePickerDialog(
                context,
                { _: DatePicker, y, mo, d ->
                    onUpdate(initial.withYear(y).withMonth(mo + 1).withDayOfMonth(d))
                    timePicker.show()
                },
                initial.year, initial.monthValue - 1, initial.dayOfMonth
            ).show()
        }
    }

    LaunchedEffect(typeError) {
        if (typeError != null) {
            focusRequester.requestFocus()
            scrollState.animateScrollTo(0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Добавить тренировку") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тип тренировки")
            types.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedTypeId = type.typeId }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedTypeId == type.typeId,
                        onClick = { selectedTypeId = type.typeId }
                    )
                    Text(text = type.name, modifier = Modifier.padding(start = 8.dp))
                }
            }

            OutlinedTextField(
                value = newTypeName,
                onValueChange = {
                    newTypeName = it
                    typeError = null
                },
                label = { Text("Новый тип тренировки") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                isError = typeError != null,
                supportingText = typeError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            Button(onClick = {
                val trimmed = newTypeName.trim()
                if (trimmed.isBlank()) {
                    typeError = "Название не может быть пустым"
                } else if (types.any { it.name.equals(trimmed, ignoreCase = true) }) {
                    typeError = "Такой тип уже существует"
                } else {
                    viewModel.addType(trimmed)
                    newTypeName = ""
                    scope.launch {
                        snackbarHostState.showSnackbar("Тип '$trimmed' добавлен")
                    }
                }
            }) {
                Text("Добавить тип")
            }

            OutlinedButton(onClick = createDateTimePicker(startTime) { startTime = it }) {
                Text("Начало: ${startTime.format(formatter)}")
            }
            OutlinedButton(onClick = createDateTimePicker(endTime) { endTime = it }) {
                Text("Конец: ${endTime.format(formatter)}")
            }

            Text("Интенсивность: ${intensity.toInt()}")
            Slider(
                value = intensity,
                onValueChange = { intensity = it },
                valueRange = 1f..10f,
                steps = 8
            )

            Button(
                onClick = {
                    selectedTypeId?.let {
                        viewModel.addSession(
                            WorkoutSession(
                                sessionId = 0L,
                                workoutTypeId = it,
                                startTime = startTime,
                                endTime = endTime,
                                intensity = intensity.toInt()
                            )
                        )
                        onBack()
                    }
                },
                enabled = selectedTypeId != null
            ) {
                Text("Сохранить")
            }
        }
    }
}
