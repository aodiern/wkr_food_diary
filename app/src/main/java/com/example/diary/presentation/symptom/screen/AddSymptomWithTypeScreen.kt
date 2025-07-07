package com.example.diary.presentation.symptom.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.diary.presentation.symptom.model.SymptomTypeUiModel
import com.example.diary.presentation.symptom.viewmodel.SymptomTypeViewModel
import com.example.diary.presentation.symptom.viewmodel.SymptomViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddSymptomWithTypeScreen(
    symptomTypeViewModel: SymptomTypeViewModel,
    symptomViewModel: SymptomViewModel,
    onSaved: () -> Unit
) {
    val context = LocalContext.current


    val types by symptomTypeViewModel.types.collectAsState(initial = emptyList())
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<SymptomTypeUiModel?>(null) }

    var showAddType by remember { mutableStateOf(false) }
    var newTypeName by remember { mutableStateOf("") }
    var newTypeError by remember { mutableStateOf<String?>(null) }

    var editingType by remember { mutableStateOf<SymptomTypeUiModel?>(null) }
    var editTypeName by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }

    var deleteType by remember { mutableStateOf<SymptomTypeUiModel?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }


    var intensity by remember { mutableStateOf(1) }


    var dateTime by remember { mutableStateOf(LocalDateTime.now()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    val timePickerDialog = remember(context) {
        TimePickerDialog(
            context,
            { _: TimePicker, hour, minute ->
                dateTime = dateTime
                    .withHour(hour)
                    .withMinute(minute)
            },
            dateTime.hour,
            dateTime.minute,
            true
        )
    }
    val datePickerDialog = remember(context) {
        DatePickerDialog(
            context,
            { _: DatePicker, year, month, day ->
                dateTime = dateTime
                    .withYear(year)
                    .withMonth(month + 1)
                    .withDayOfMonth(day)
                timePickerDialog.show()
            },
            dateTime.year,
            dateTime.monthValue - 1,
            dateTime.dayOfMonth
        )
    }


    if (showEditDialog && editingType != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Редактировать тип") },
            text = {
                OutlinedTextField(
                    value = editTypeName,
                    onValueChange = { editTypeName = it; newTypeError = null },
                    label = { Text("Название") },
                    isError = editTypeName.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (editTypeName.isBlank()) {
                    Text("Не может быть пустым", color = MaterialTheme.colorScheme.error)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editTypeName.isNotBlank()) {
                        symptomTypeViewModel.editType(
                            editingType!!.typeId,
                            editTypeName.trim()
                        )
                        showEditDialog = false
                    }
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }


    if (showDeleteConfirm && deleteType != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Удалить тип?") },
            text = { Text("Вы точно хотите удалить «${deleteType!!.name}»?") },
            confirmButton = {
                TextButton(onClick = {
                    symptomTypeViewModel.removeType(deleteType!!.typeId)
                    if (selectedType == deleteType) selectedType = null
                    showDeleteConfirm = false
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Тип симптома", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = selectedType?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Выберите тип") },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            types.forEach { type ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedType = type
                            expanded = false
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(type.name, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        editingType = type
                        editTypeName = type.name
                        showEditDialog = true
                        expanded = false
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                    }
                    IconButton(onClick = {
                        deleteType = type
                        showDeleteConfirm = true
                        expanded = false
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                    }
                }
            }
            Divider()
            DropdownMenuItem(
                text = { Text("Добавить новый тип") },
                onClick = {
                    showAddType = true
                    expanded = false
                }
            )
        }

        if (showAddType) {
            OutlinedTextField(
                value = newTypeName,
                onValueChange = {
                    newTypeName = it
                    newTypeError = null
                },
                label = { Text("Название нового типа") },
                isError = newTypeError != null,
                modifier = Modifier.fillMaxWidth()
            )
            newTypeError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    if (newTypeName.isBlank()) {
                        newTypeError = "Не может быть пустым"
                    } else {
                        symptomTypeViewModel.addType(newTypeName.trim())
                        types.lastOrNull()?.let { selectedType = it }
                        newTypeName = ""
                        showAddType = false
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сохранить тип")
            }
        }

        Divider()


        Text("Интенсивность: $intensity")
        Slider(
            value = intensity.toFloat(),
            onValueChange = { intensity = it.toInt() },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = dateTime.format(formatter),
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата и время") },
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Выбрать дату и время",
                    modifier = Modifier.clickable { datePickerDialog.show() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
        )

        Spacer(Modifier.height(24.dp))


        Button(
            onClick = {
                selectedType?.let {
                    symptomViewModel.addSymptom(it.typeId, intensity, dateTime)
                    onSaved()
                }
            },
            enabled = selectedType != null,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Добавить симптом")
        }
    }
}
