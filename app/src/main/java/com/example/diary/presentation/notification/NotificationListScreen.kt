package com.example.diary.presentation.notification

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.notification.ReminderEditDialog
import com.example.diary.presentation.combined.DiaryTopAppBar
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    viewModel: ReminderListViewModel = hiltViewModel()
) {

    val reminders by viewModel.reminders.collectAsState()


    var editingReminder by remember { mutableStateOf<ScheduledReminder?>(null) }
    var isNew by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { DiaryTopAppBar("Настройка уведомлений")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    isNew = true
                    editingReminder = ScheduledReminder(
                        id = 0,
                        message = "",
                        timeInMillis = System.currentTimeMillis(),
                        repeatInterval = RepeatInterval.DAILY
                    )
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить уведомление")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(reminders) { rem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(rem.message, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    .format(Date(rem.timeInMillis)),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(rem.repeatInterval.name, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = {

                            isNew = false
                            editingReminder = rem
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                        }
                        IconButton(onClick = {
                            viewModel.deleteReminder(rem)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    }
                }
            }
        }
    }


    editingReminder?.let { rem ->
        ReminderEditDialog(
            initial = rem,
            isNew = isNew,
            onConfirm = { updated ->
                if (isNew) viewModel.addReminder(updated)
                else viewModel.updateReminder(updated)
                editingReminder = null
            },
            onDismiss = {
                editingReminder = null
            }
        )
    }
}