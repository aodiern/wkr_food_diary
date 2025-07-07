package com.example.diary.presentation.combined.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diary.data.local.entity.DiaryEntry
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ChartSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            content()
        }
    }
}

@Composable
fun WeekPicker(
    weekStart: LocalDate,
    weekEnd: LocalDate,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "‹",
            modifier = Modifier.clickable(onClick = onPrev),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "${weekStart.dayOfMonth} ${weekStart.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}" +
                    " — " +
                    "${weekEnd.dayOfMonth} ${weekEnd.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "›",
            modifier = Modifier.clickable(onClick = onNext),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

fun sleepByDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, Double> {
    val weekEnd = weekStart.plusDays(6)
    return entries
        .filter { it.date in weekStart..weekEnd }
        .groupBy { it.date }
        .mapValues { (_, day) ->
            day.sumOf { it.sleepDuration.toMinutes().toDouble() } / 60.0
        }
}