package com.example.diary.notification

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.diary.presentation.notification.RepeatInterval
import com.example.diary.presentation.notification.ScheduledReminder
import java.util.Calendar as JCalendar

@Composable
fun ReminderEditDialog(
    initial: ScheduledReminder,
    isNew: Boolean,
    onConfirm: (ScheduledReminder) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initial.message) }
    var hour by remember {
        mutableStateOf(
            JCalendar.getInstance().apply { timeInMillis = initial.timeInMillis }
                .get(JCalendar.HOUR_OF_DAY)
        )
    }
    var minute by remember {
        mutableStateOf(
            JCalendar.getInstance().apply { timeInMillis = initial.timeInMillis }
                .get(JCalendar.MINUTE)
        )
    }
    var interval by remember { mutableStateOf(initial.repeatInterval) }
    var customDaysInterval by remember { mutableStateOf(1) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isNew) "Новое уведомление" else "Редактировать")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Текст") },
                    singleLine = true
                )


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Время: %02d:%02d".format(hour, minute))
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        TimePickerDialog(
                            context,
                            { _, h, m ->
                                hour = h
                                minute = m
                            },
                            hour,
                            minute,
                            true
                        ).show()
                    }) {
                        Text("Выбрать")
                    }
                }


                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text("Интервал: ${interval.name}")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        RepeatInterval.values().forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt.name) },
                                onClick = {
                                    interval = opt
                                    expanded = false
                                }
                            )
                        }
                    }
                }


                if (interval == RepeatInterval.CUSTOM) {
                    OutlinedTextField(
                        value = customDaysInterval.toString(),
                        onValueChange = { value ->
                            customDaysInterval = value.toIntOrNull() ?: 1
                        },
                        label = { Text("Каждые X дней") },
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {

                val cal = JCalendar.getInstance().apply {
                    set(JCalendar.HOUR_OF_DAY, hour)
                    set(JCalendar.MINUTE, minute)
                    set(JCalendar.SECOND, 0)
                }
                onConfirm(
                    ScheduledReminder(
                        id = initial.id,
                        message = text,
                        timeInMillis = cal.timeInMillis,
                        repeatInterval = interval
                    )
                )
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
