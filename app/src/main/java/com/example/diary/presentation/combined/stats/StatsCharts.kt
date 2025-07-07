
package com.example.diary.presentation.combined.stats

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.diary.domain.model.stats.DailySleepStat
import com.example.diary.domain.model.stats.DailyWaterStat
import com.example.diary.domain.model.stats.WaterCategoryStat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.map

@Composable
fun SleepChart(
    data: List<DailySleepStat>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            val date = LocalDate.ofEpochDay(value.toLong())
                            date.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))
                        } catch (e: Exception) {
                            value.toInt().toString()
                        }
                    }
                }
                axisRight.isEnabled = false
                animateY(500)
            }
        },
        modifier = modifier
    ) { chart ->
        val entries = data.map { stat ->
            Entry(stat.date.toEpochDay().toFloat(), stat.totalDurationHours.toFloat())
        }

        val dataSet = LineDataSet(entries, "Часы сна").apply {
            valueTextSize = 10f
            circleRadius = 4f
            setDrawValues(false)
        }

        chart.data = LineData(dataSet)
        chart.invalidate()
    }
}


@Composable
fun WaterChart(
    data: List<DailyWaterStat>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                Log.d("StatsCharts", "Updating chart with ${data.size} points")
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                animateY(500)
            }
        },
        modifier = modifier
    ) { chart ->

        val entries = data.map { stat ->
            Entry(
                stat.date.toEpochDay().toFloat(),
                stat.totalAmountMl.toFloat()
            )
        }
        val dataSet = LineDataSet(entries, "Вода (мл)")
        chart.data = LineData(dataSet)
        chart.invalidate()
    }
}

@Composable
fun WaterCategoryChart(
    data: List<WaterCategoryStat>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                setUsePercentValues(true)
                centerText = "По напиткам"
                setEntryLabelTextSize(12f)
                legend.isEnabled = false
            }
        },
        modifier = modifier
    ) { chart ->
        val entries = data.map { stat ->
            val label = try {
                val parsed = LocalDate.parse(stat.category)
                parsed.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))
            } catch (e: Exception) {
                stat.category
            }
            PieEntry(stat.totalAmountMl.toFloat(), label)
        }

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 2f
            valueTextSize = 12f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()} мл"
                }
            }
        }

        chart.data = PieData(dataSet)
        chart.invalidate()
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWaterChart() {
    val sample = listOf(
        DailyWaterStat(LocalDate.now().minusDays(3), 1500, 3),
        DailyWaterStat(LocalDate.now().minusDays(2), 1800, 4),
        DailyWaterStat(LocalDate.now().minusDays(1), 1700, 4),
        DailyWaterStat(LocalDate.now(),           2000, 5),
    )
    WaterChart(data = sample)
}

@Preview(showBackground = true)
@Composable
fun PreviewSleepChart() {
    val sampleSleepData = listOf(
        DailySleepStat(LocalDate.now().minusDays(4), 7.5, 80.0),
        DailySleepStat(LocalDate.now().minusDays(3), 6.0, 75.0),
        DailySleepStat(LocalDate.now().minusDays(2), 8.0, 85.0),
        DailySleepStat(LocalDate.now().minusDays(1), 7.0, 78.0),
        DailySleepStat(LocalDate.now(), 6.5, 82.0)
    )
    SleepChart(data = sampleSleepData)
}


@Preview(showBackground = true)
@Composable
fun PreviewWaterCategoryChart() {
    val sampleWaterCategoryData = listOf(
        WaterCategoryStat("Чай", 600),
        WaterCategoryStat("Кофе", 300),
        WaterCategoryStat("Сок", 400),
        WaterCategoryStat("Вода", 1200),
    )
    WaterCategoryChart(data = sampleWaterCategoryData)
}
