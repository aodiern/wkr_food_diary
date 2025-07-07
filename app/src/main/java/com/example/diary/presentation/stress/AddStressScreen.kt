package com.example.diary.presentation.stress

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.combined.DiaryTopAppBar
import com.example.diary.presentation.stress.StressLogViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStressScreen(
    viewModel: StressLogViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var level by remember { mutableStateOf(5f) }
    var dateTime by remember { mutableStateOf(LocalDateTime.now()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val stressColor = when (level.toInt()) {
        in 1..3 -> Color(0xFF4CAF50)
        in 4..7 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    val stressEmoji = when (level.toInt()) {
        in 1..3 -> "🙂"
        in 4..7 -> "😐"
        else -> "😣"
    }

    val timePicker = remember(context) {
        TimePickerDialog(
            context,
            { _: TimePicker, h, m -> dateTime = dateTime.withHour(h).withMinute(m) },
            dateTime.hour, dateTime.minute, true
        )
    }

    val datePicker = remember(context) {
        DatePickerDialog(
            context,
            { _: DatePicker, y, mo, d ->
                dateTime = dateTime.withYear(y).withMonth(mo + 1).withDayOfMonth(d)
                timePicker.show()
            },
            dateTime.year, dateTime.monthValue - 1, dateTime.dayOfMonth
        )
    }

    Scaffold(
        topBar = { DiaryTopAppBar("Добавить стресс") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "$stressEmoji Уровень стресса: ${level.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                color = stressColor
            )
            Slider(
                value = level,
                onValueChange = { level = it },
                valueRange = 1f..10f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = stressColor,
                    activeTrackColor = stressColor
                )
            )

            OutlinedButton(onClick = { datePicker.show() }) {
                Text("Дата и время: ${dateTime.format(formatter)}")
            }

            Button(onClick = {
                viewModel.addLog(level.toInt(), dateTime)
                onBack()
            }) {
                Text("Сохранить")
            }
        }
    }
}
