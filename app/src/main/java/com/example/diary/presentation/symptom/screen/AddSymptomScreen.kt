package com.example.diary.presentation.symptom.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diary.presentation.symptom.model.SymptomTypeUiModel
import com.example.diary.presentation.symptom.viewmodel.SymptomViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSymptomScreen(
    viewModel: SymptomViewModel,
    onSaved: () -> Unit
) {

    val types: List<SymptomTypeUiModel> by
    viewModel.types.collectAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<SymptomTypeUiModel?>(null) }
    var intensity by remember { mutableStateOf(1) }
    var dateTime by remember { mutableStateOf(LocalDateTime.now()) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Text(
        text = "Добавить симптом",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedType?.name.orEmpty(),
                onValueChange = {},
                readOnly = true,
                label = { Text("Тип симптома") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.fillMaxWidth()
            )


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Интенсивность: $intensity")
        Slider(
            value = intensity.toFloat(),
            onValueChange = { intensity = it.toInt() },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = dateTime.format(formatter),
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата и время") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* открыть диалог выбора даты/времени */ }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                selectedType?.let {
                    viewModel.addSymptom(it.typeId, intensity, dateTime)
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
