package com.example.diary.presentation.sleep

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.domain.model.SleepSession
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTrackerScreen(
    viewModel: SleepViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val sessions by viewModel.sessions.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ISO_DATE

    val datePicker = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d -> viewModel.loadSessions(LocalDate.of(y, m+1, d)) },
            selectedDate.year,
            selectedDate.monthValue-1,
            selectedDate.dayOfMonth
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Трекер сна") },
                actions = {
                    Text(
                        selectedDate.format(dateFormatter),
                        modifier = Modifier
                            .clickable { datePicker.show() }
                            .padding(end = 16.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить запись")
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(sessions) { session ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Сон с ${session.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} до ${session.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            session.quality?.let {
                                Text("Качество: $it/10", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        IconButton(onClick = { viewModel.deleteSession(session.sessionId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    }
                }
            }
        }

        if (showDialog) {
            SleepSessionDialog(
                onDismiss = { showDialog = false },
                onSave = { start, end, quality ->
                    viewModel.addSession(
                        SleepSession(
                            sessionId = 0L,
                            startTime = start.truncatedTo(ChronoUnit.MINUTES),
                            endTime = end.truncatedTo(ChronoUnit.MINUTES),
                            quality = quality
                        )
                    )
                    showDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepSessionDialog(
    onDismiss: () -> Unit,
    onSave: (LocalDateTime, LocalDateTime, Int?) -> Unit
) {
    val context = LocalContext.current
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var endTime by remember { mutableStateOf(LocalTime.now().plusHours(8).truncatedTo(ChronoUnit.MINUTES)) }
    var quality by remember { mutableStateOf(5) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    val datePickerStart = remember {
        DatePickerDialog(context, { _, y, m, d -> startDate = LocalDate.of(y, m+1, d) }, startDate.year, startDate.monthValue-1, startDate.dayOfMonth)
    }
    val timePickerStart = remember {
        TimePickerDialog(context, { _, h, mi -> startTime = LocalTime.of(h, mi) }, startTime.hour, startTime.minute, true)
    }
    val datePickerEnd = remember {
        DatePickerDialog(context, { _, y, m, d -> endDate = LocalDate.of(y, m+1, d) }, endDate.year, endDate.monthValue-1, endDate.dayOfMonth)
    }
    val timePickerEnd = remember {
        TimePickerDialog(context, { _, h, mi -> endTime = LocalTime.of(h, mi) }, endTime.hour, endTime.minute, true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая сессия сна") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = startDate.format(dateFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата начала") },
                    trailingIcon = {
                        IconButton(onClick = { datePickerStart.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = startTime.format(timeFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Время начала") },
                    trailingIcon = {
                        IconButton(onClick = { timePickerStart.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                OutlinedTextField(
                    value = endDate.format(dateFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата окончания") },
                    trailingIcon = {
                        IconButton(onClick = { datePickerEnd.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = endTime.format(timeFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Время окончания") },
                    trailingIcon = {
                        IconButton(onClick = { timePickerEnd.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Text("Качество сна: $quality/10")
                Slider(
                    value = quality.toFloat(),
                    onValueChange = { quality = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val start = LocalDateTime.of(startDate, startTime).truncatedTo(ChronoUnit.MINUTES)
                val end = LocalDateTime.of(endDate, endTime).truncatedTo(ChronoUnit.MINUTES)
                if (end.isAfter(start)) onSave(start, end, quality)
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
