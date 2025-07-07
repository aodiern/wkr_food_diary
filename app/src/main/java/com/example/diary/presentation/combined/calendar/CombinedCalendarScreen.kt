package com.example.diary.presentation.combined.calendar

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.combined.DiaryTopAppBar
import com.example.diary.presentation.combined.calendar.CombinedCalendarFilterViewModel.CombinedEntryDetail
import com.example.diary.presentation.medication.viewmodel.MedicationDefinitionViewModel
import com.example.diary.presentation.medication.viewmodel.MedicationTrackerViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinedCalendarScreen(
    vm: CombinedCalendarFilterViewModel = hiltViewModel()
) {
    val medVm: MedicationTrackerViewModel = hiltViewModel()
    val medLogs by medVm.logs.collectAsState(initial = emptyList())

    val startDate by vm.startDate.collectAsState()
    val endDate by vm.endDate.collectAsState()
    val query by vm.searchQuery.collectAsState()
    val entries by vm.filteredEntries.collectAsState()

    LaunchedEffect(startDate) { medVm.loadLogsFor(startDate) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val startPicker = remember {
        DatePickerDialog(
            context,
            { _, y, m, d -> vm.setStartDate(LocalDate.of(y, m + 1, d)) },
            startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
        )
    }
    val endPicker = remember {
        DatePickerDialog(
            context,
            { _, y, m, d -> vm.setEndDate(LocalDate.of(y, m + 1, d)) },
            endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
        )
    }

    val medDefVm: MedicationDefinitionViewModel = hiltViewModel()
    val medDefs by medDefVm.definitions.collectAsState(initial = emptyList())
    val medEntries = medLogs
        .filter { it.scheduledTime.toLocalDate() in startDate..endDate }
        .map { log ->
            val defName = medDefs.firstOrNull { it.id == log.planId }?.name.orEmpty()
            CombinedEntryDetail.Medication(
                id = log.id,
                dateTime = log.scheduledTime,
                name = defName.ifEmpty { log.name },
                dose = log.dose,
                taken = log.taken
            )
        }

    val combinedEntries = (entries.filterNot { it is CombinedEntryDetail.Medication } + medEntries)
        .sortedByDescending { it.dateTime }

    Scaffold(topBar = { DiaryTopAppBar("Журнал записей") }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = startDate.format(dateFormatter),
                    onValueChange = {}, readOnly = true,
                    label = { Text("С") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            Modifier.clickable { startPicker.show() }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = endDate.format(dateFormatter),
                    onValueChange = {}, readOnly = true,
                    label = { Text("По") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            Modifier.clickable { endPicker.show() }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = query,
                onValueChange = { vm.setSearchQuery(it) },
                label = { Text("Поиск") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(combinedEntries) { entry ->
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(entry.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                entry.dateTime.format(dateTimeFormatter),
                                style = MaterialTheme.typography.bodySmall
                            )
                            when (entry) {
                                is CombinedEntryDetail.Meal ->
                                    Text("Метод: ${entry.method}", style = MaterialTheme.typography.bodySmall)
                                is CombinedEntryDetail.Symptom ->
                                    Text("Интенсивность: ${entry.intensity}", style = MaterialTheme.typography.bodySmall)
                                is CombinedEntryDetail.Medication -> {
                                    Text("Название: ${entry.name}", style = MaterialTheme.typography.bodySmall)
                                    Text("Дозировка: ${entry.dose}", style = MaterialTheme.typography.bodySmall)
                                }
                                is CombinedEntryDetail.Water ->
                                    Text("Объём: ${entry.amountMl} мл", style = MaterialTheme.typography.bodySmall)
                                is CombinedEntryDetail.Sleep ->
                                    Text("Длительность: ${entry.start.toLocalTime()}–${entry.end.toLocalTime()}", style = MaterialTheme.typography.bodySmall)
                                is CombinedEntryDetail.Workout ->
                                    Text("Интенсивность: ${entry.intensity}/10", style = MaterialTheme.typography.bodySmall)
                                is CombinedEntryDetail.Stress ->
                                    Text("Уровень: ${entry.level}/10", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
