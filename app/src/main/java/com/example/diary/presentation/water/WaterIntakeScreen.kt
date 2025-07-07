package com.example.diary.presentation.water

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
import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.presentation.combined.DiaryTopAppBar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterIntakeScreen(
    viewModel: WaterIntakeViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val intakes by viewModel.intakes.collectAsState()

    var showAddIntake by remember { mutableStateOf(false) }
    var showAddCategory by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { DiaryTopAppBar("Питьевой режим") },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddIntake = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить приём")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            if (intakes.isEmpty()) {
                item {
                    Text(
                        "Нет записей на эту дату",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(intakes) { intake ->
                    WaterIntakeItem(
                        intake = intake,
                        category = categories.firstOrNull { it.categoryId == intake.categoryId },
                        onDelete = { viewModel.deleteIntake(intake.intakeId) }
                    )
                }
            }
        }

        if (showAddIntake) {
            AddIntakeDialog(
                categories = categories,
                onAddCategory = { showAddCategory = true },
                onDismiss = { showAddIntake = false },
                onSave = { amount, categoryId, dateTime ->
                    viewModel.addIntake(
                        WaterIntake(
                            intakeId = 0L,
                            amountMl = amount,
                            categoryId = categoryId,
                            timestamp = dateTime
                        )
                    )
                    showAddIntake = false
                }
            )
        }

        if (showAddCategory) {
            var name by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showAddCategory = false },
                title = { Text("Новая категория") },
                text = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Название категории") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (name.isNotBlank()) {
                            viewModel.addCategory(BeverageCategory(0L, name.trim()))
                            showAddCategory = false
                        }
                    }) { Text("Сохранить") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddCategory = false }) { Text("Отмена") }
                }
            )
        }
    }
}

@Composable
fun WaterIntakeItem(
    intake: WaterIntake,
    category: BeverageCategory?,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${intake.amountMl} мл",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${category?.name.orEmpty()} — ${intake.timestamp.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIntakeDialog(
    categories: List<BeverageCategory>,
    onAddCategory: () -> Unit,
    onDismiss: () -> Unit,
    onSave: (amount: Int, categoryId: Long, dateTime: LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<BeverageCategory?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var dateTime by remember { mutableStateOf(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    val datePicker = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d ->
                dateTime = dateTime.withYear(y).withMonth(m + 1).withDayOfMonth(d)
            },
            dateTime.year,
            dateTime.monthValue - 1,
            dateTime.dayOfMonth
        )
    }
    val timePicker = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, h, min ->
                dateTime = dateTime.withHour(h).withMinute(min)
            },
            dateTime.hour,
            dateTime.minute,
            true
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить приём") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter(Char::isDigit) },
                    label = { Text("Объём (мл)") },
                    modifier = Modifier.fillMaxWidth()
                )


                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Категория") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    selectedCategory = cat
                                    expanded = false
                                }
                            )
                        }
                        Divider()
                        DropdownMenuItem(
                            text = { Text("+ Категория") },
                            onClick = {
                                expanded = false
                                onAddCategory()
                            }
                        )
                    }
                }


                OutlinedTextField(
                    value = dateTime.format(dateFormatter),
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
                    value = dateTime.format(timeFormatter),
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
                val amt = amountText.toIntOrNull() ?: 0
                selectedCategory?.let { cat ->
                    if (amt > 0) onSave(amt, cat.categoryId, dateTime)
                }
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}