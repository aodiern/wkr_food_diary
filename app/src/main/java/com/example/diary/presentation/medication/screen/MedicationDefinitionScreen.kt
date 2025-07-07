package com.example.diary.presentation.medication.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.presentation.medication.viewmodel.MedicationDefinitionViewModel
import kotlin.collections.listOfNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDefinitionScreen(
    vm: MedicationDefinitionViewModel = hiltViewModel()
) {

    val definitions by vm.definitions.collectAsState()
    var editDef by remember { mutableStateOf<MedicationDefinition?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Справочник лекарств") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {

                editDef = MedicationDefinition(0L, "", null, null)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
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
                items(definitions) { def ->
                    ListItem(
                        headlineContent = { Text(def.name) },
                        supportingContent = {
                            Text(
                                text = listOfNotNull(def.form, def.strength)
                                    .joinToString(", ")
                            )
                        },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { editDef = def }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                                }
                                IconButton(onClick = { vm.removeDefinition(def.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    )
                    Divider()
                }
            }


            editDef?.let { def0 ->
                DefinitionDialog(
                    def = def0,
                    onDismiss = { editDef = null },
                    onSave = { name, form, strength ->
                        if (def0.id == 0L) {

                            vm.createDefinition(
                                def0.copy(name = name, form = form, strength = strength)
                            )
                        } else {

                            vm.editDefinition(
                                def0.copy(name = name, form = form, strength = strength)
                            )
                        }
                        editDef = null
                    }
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefinitionDialog(
    def: MedicationDefinition,
    onDismiss: () -> Unit,
    onSave: (name: String, form: String?, strength: String?) -> Unit
) {
    var name by remember { mutableStateOf(def.name) }
    var form by remember { mutableStateOf(def.form.orEmpty()) }
    var strength by remember { mutableStateOf(def.strength.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (def.id == 0L) "Добавить лекарство" else "Редактировать лекарство")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = form,
                    onValueChange = { form = it },
                    label = { Text("Форма выпуска") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = strength,
                    onValueChange = { strength = it },
                    label = { Text("Концентрация") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    onSave(
                        name.trim(),
                        form.trim().takeIf { it.isNotBlank() },
                        strength.trim().takeIf { it.isNotBlank() }
                    )
                }
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
