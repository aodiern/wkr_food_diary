package com.example.diary.presentation.medication.screen

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.medication.viewmodel.MedicationTrackerViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationTrackerScreen(
    vm: MedicationTrackerViewModel = hiltViewModel()
) {
    val logs by vm.logs.collectAsState()
    var showAdHoc by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.loadLogsFor(LocalDate.now())
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Трекер приёма") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdHoc = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                Text(
                    "Плановые приёмы",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(logs.filter { it.planId != null }) { log ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Checkbox(log.taken, onCheckedChange = { vm.toggleTaken(log) })
                    Text(
                        text = "${log.scheduledTime.toLocalDate()} ${log.scheduledTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))}  ${log.name}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Text(
                    "Разовые приёмы",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(logs.filter { it.planId == null }) { log ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Checkbox(log.taken, onCheckedChange = { vm.toggleTaken(log) })
                    Text(
                        text = "${log.scheduledTime.toLocalDate()} ${log.scheduledTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))}  ${log.name}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        if (showAdHoc) {
            AdHocDialog(
                onDismiss = { showAdHoc = false },
                onSave = { name, dose, dt ->
                    vm.addAdHocLog(name, dose, dt)
                    showAdHoc = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdHocDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, dose: String, dateTime: LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.now()) }
    val dateFormatter = DateTimeFormatter.ISO_DATE
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    val datePicker = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d -> date = LocalDate.of(y, m + 1, d) },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )
    }
    val timePicker = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, h, m -> time = LocalTime.of(h, m) },
            time.hour,
            time.minute,
            true
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Разовый приём") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dose,
                    onValueChange = { dose = it },
                    label = { Text("Дозировка") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = date.format(dateFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата") },
                    trailingIcon = {
                        IconButton(onClick = { datePicker.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = time.format(timeFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Время") },
                    trailingIcon = {
                        IconButton(onClick = { timePicker.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(name.trim(), dose.trim(), LocalDateTime.of(date, time))
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
