package com.example.diary.presentation.symptom.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items        // ← вот оно!
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diary.presentation.symptom.model.SymptomTypeUiModel
import com.example.diary.presentation.symptom.viewmodel.SymptomTypeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomTypeListScreen(
    viewModel: SymptomTypeViewModel,
    onNavigateToAdd: () -> Unit
) {
    val types by viewModel.types.collectAsState()
    var toEdit by remember { mutableStateOf<SymptomTypeUiModel?>(null) }
    var toDelete by remember { mutableStateOf<SymptomTypeUiModel?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список типов симптомов") },
                actions = {
                    IconButton(onClick = onNavigateToAdd) {
                        Icon(Icons.Default.Edit, contentDescription = "Добавить")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(types) { type ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(type.name, style = MaterialTheme.typography.titleMedium)
                        Row {
                            IconButton(onClick = { toEdit = type }) {
                                Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                            }
                            IconButton(onClick = { toDelete = type }) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить")
                            }
                        }
                    }
                }
            }
        }
    }

    toEdit?.let { type ->
        AlertDialog(
            onDismissRequest = { toEdit = null },
            title = { Text("Редактировать тип") },
            text = {
                var name by remember { mutableStateOf(type.name) }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название типа") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.editType(type.typeId, /* newName */ TODO())
                    toEdit = null
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { toEdit = null }) {
                    Text("Отмена")
                }
            }
        )
    }

    toDelete?.let { type ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Удалить тип") },
            text = { Text("Удалить \"${type.name}\"? Это приведет к удалению связанных симптомов.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeType(type.typeId)
                    toDelete = null
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}