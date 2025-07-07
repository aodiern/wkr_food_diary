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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.presentation.medication.viewmodel.MedicationPlanViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationPlanScreen(
    vm: MedicationPlanViewModel = hiltViewModel(),
    defs: List<MedicationDefinition>
) {
    val plans by vm.plans.collectAsState()
    var editPlan by remember { mutableStateOf<MedicationPlan?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Планы приёма") }) },
        floatingActionButton = {
            if (defs.isNotEmpty()) {
                FloatingActionButton(onClick = {

                    editPlan = MedicationPlan(
                        id = 0L,
                        definitionId = defs.first().id,
                        dosage = "",
                        timesPerDay = 1,
                        times = listOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)),
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusDays(7)
                    )
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить план")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(plans) { plan ->
                    ListItem(
                        headlineContent = {
                            Text(defs.firstOrNull { it.id == plan.definitionId }?.name.orEmpty())
                        },
                        supportingContent = {
                            Text(
                                "${plan.dosage} • " +
                                        plan.times.joinToString { it.format(DateTimeFormatter.ofPattern("HH:mm")) }
                            )
                        },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { editPlan = plan }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Изменить")
                                }
                                IconButton(onClick = { vm.removePlan(plan.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    )
                    Divider()
                }
            }

            editPlan?.let { plan0 ->
                PlanDialog(
                    defs = defs,
                    plan = plan0,
                    onDismiss = { editPlan = null },
                    onSave = { defId, dosage, times, startDate, endDate ->
                        vm.createPlan(
                            plan0.copy(
                                definitionId = defId,
                                dosage = dosage,
                                timesPerDay = times.size,
                                times = times,
                                startDate = startDate,
                                endDate = endDate
                            )
                        )
                        editPlan = null
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDialog(
    defs: List<MedicationDefinition>,
    plan: MedicationPlan,
    onDismiss: () -> Unit,
    onSave: (
        definitionId: Long,
        dosage: String,
        times: List<LocalTime>,
        startDate: LocalDate,
        endDate: LocalDate
    ) -> Unit
) {
    val context = LocalContext.current
    var defId by remember { mutableStateOf(plan.definitionId) }
    var dosage by remember { mutableStateOf(plan.dosage) }
    val times = remember { mutableStateListOf<LocalTime>().apply { addAll(plan.times) } }
    var expanded by remember { mutableStateOf(false) }

    var startDate by remember { mutableStateOf(plan.startDate) }
    var startTime by remember { mutableStateOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)) }
    var endDate by remember { mutableStateOf(plan.endDate ?: plan.startDate.plusDays(7)) }
    var endTime by remember { mutableStateOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES)) }


    val addTimePicker = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, h, mi ->
                val t = LocalTime.of(h, mi).truncatedTo(ChronoUnit.MINUTES)
                times.add(t)
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            true
        )
    }


    val datePickerStart = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d -> startDate = LocalDate.of(y, m + 1, d) },
            startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
        )
    }
    val timePickerStart = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, h, mi -> startTime = LocalTime.of(h, mi) },
            startTime.hour, startTime.minute, true
        )
    }
    val datePickerEnd = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d -> endDate = LocalDate.of(y, m + 1, d) },
            endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
        )
    }
    val timePickerEnd = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, h, mi -> endTime = LocalTime.of(h, mi) },
            endTime.hour, endTime.minute, true
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (plan.id == 0L) "Добавить план" else "Редактировать план") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = defs.firstOrNull { it.id == defId }?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Лекарство") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        defs.forEach { def ->
                            DropdownMenuItem(text = { Text(def.name) }, onClick = {
                                defId = def.id
                                expanded = false
                            })
                        }
                    }
                }


                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Дозировка") },
                    modifier = Modifier.fillMaxWidth()
                )


                Text("Время приёма:", style = MaterialTheme.typography.bodyMedium)
                times.forEach { t ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text(t.format(DateTimeFormatter.ofPattern("HH:mm")), modifier = Modifier.weight(1f))
                        IconButton(onClick = { times.remove(t) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
                TextButton(onClick = { addTimePicker.show() }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Добавить время")
                }


                OutlinedTextField(
                    value = LocalDateTime.of(startDate, startTime)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Начало курса") },
                    trailingIcon = {
                        IconButton(onClick = {
                            datePickerStart.show()
                            timePickerStart.show()
                        }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                OutlinedTextField(
                    value = LocalDateTime.of(endDate, endTime)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Окончание курса") },
                    trailingIcon = {
                        IconButton(onClick = {
                            datePickerEnd.show()
                            timePickerEnd.show()
                        }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(defId, dosage.trim(), times.toList(), startDate, endDate)
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}
