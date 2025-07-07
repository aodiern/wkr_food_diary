package com.example.diary.presentation.combined.stats

import android.graphics.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import android.graphics.Color as AndroidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.combined.DiaryTopAppBar
import com.example.diary.ui.theme.DiaryTheme
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate


@Composable
fun ChartCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ComposeColor(0xFFFFFFFF)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    DiaryTheme {
        val weekStart by viewModel.weekStart.collectAsState(initial = LocalDate.now())
        val weekEnd = weekStart.plusDays(6)
        val sleepMap by viewModel.sleepByDay.collectAsState(initial = emptyMap())
        val qualityMap by viewModel.sleepQualityByDay.collectAsState(initial = emptyMap())
        val drinks by viewModel.drinksByCategory.collectAsState(initial = emptyMap())
        val stress by viewModel.maxStressByDay.collectAsState(initial = emptyMap())
        val workoutsMap by viewModel.workoutsByCategory.collectAsState(initial = emptyMap())
        val meds by viewModel.medicationsTaken.collectAsState(initial = emptyMap())
        val symptomsMap by viewModel.symptomsByDay.collectAsState(initial = emptyMap())
        val fluid by viewModel.fluidByDay.collectAsState(initial = emptyMap())
        val workoutsByDay by viewModel.workoutsByDay.collectAsState(initial = emptyMap())
        val avgIntensity by viewModel.avgWorkoutIntensity.collectAsState(initial = emptyMap())
        val symptomIntensity by viewModel.symptomIntensityByType.collectAsState(initial = emptyMap())
        val fastingByDay by viewModel.longestFastingByDay.collectAsState(initial = emptyMap())

        Scaffold(
            topBar = { DiaryTopAppBar("Статистика") }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                WeekPicker(
                    weekStart = weekStart,
                    weekEnd = weekEnd,
                    onPrev = { viewModel.changeWeek(-1) },
                    onNext = { viewModel.changeWeek(1) }
                )


                if (sleepMap.isNotEmpty()) ChartCard("Часы сна за неделю") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            LineChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                animateX(600, Easing.EaseInOutCubic)
                                setDrawGridBackground(false)
                                xAxis.setDrawGridLines(false)
                                axisLeft.setDrawGridLines(false)
                                axisRight.isEnabled = false

                                val entries = sleepMap.entries.sortedBy { it.key }
                                    .mapIndexed { i, (_, hours) -> Entry(i.toFloat(), hours.toFloat()) }
                                data = LineData(
                                    LineDataSet(entries, "").apply {
                                        lineWidth = 2f
                                        setColors(*ColorTemplate.MATERIAL_COLORS)
                                        valueTextSize = 14f
                                        valueTextColor = android.graphics.Color.BLACK
                                        setDrawValues(true)
                                        color = AndroidColor.parseColor("#EF7A2C")
                                    }
                                )
                                description.isEnabled = false
                                xAxis.apply {
                                    granularity = 1f
                                    textSize = 12f
                                    valueFormatter = IndexAxisValueFormatter(
                                        sleepMap.keys.sorted().map { it.dayOfWeek.name.take(3) }
                                    )
                                }
                                legend.isEnabled = false
                            }
                        }
                    )
                }


                if (stress.isNotEmpty()) ChartCard("Стресс по дням") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            LineChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                animateX(600, Easing.EaseInOutCubic)
                                xAxis.setDrawGridLines(false)
                                axisLeft.setDrawGridLines(false)
                                axisRight.isEnabled = false

                                val entries = stress.entries.sortedBy { it.key }
                                    .mapIndexed { i, (_, level) -> Entry(i.toFloat(), level.toFloat()) }
                                data = LineData(
                                    LineDataSet(entries, "").apply {
                                        lineWidth = 2f
                                        setColors(*ColorTemplate.VORDIPLOM_COLORS)
                                        valueTextSize = 14f
                                        valueTextColor = android.graphics.Color.BLACK
                                        setDrawValues(true)
                                        valueFormatter = DefaultValueFormatter(0)
                                    }
                                )
                                description.isEnabled = false
                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(
                                        stress.keys.sorted().map { it.dayOfWeek.name.take(3) }
                                    )
                                }
                                legend.isEnabled = false
                            }
                        }
                    )
                }


                if (qualityMap.isNotEmpty()) ChartCard("Качество сна по дням") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            LineChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                animateX(600, Easing.EaseInOutCubic)
                                xAxis.setDrawGridLines(false)
                                axisLeft.setDrawGridLines(false)
                                axisRight.isEnabled = false

                                val entries = qualityMap.entries.sortedBy { it.key }
                                    .mapIndexed { i, (_, q) -> Entry(i.toFloat(), q.toFloat()) }
                                data = LineData(
                                    LineDataSet(entries, "").apply {
                                        lineWidth = 2f
                                        setColors(*ColorTemplate.COLORFUL_COLORS)
                                        valueTextSize = 14f
                                        valueTextColor = android.graphics.Color.BLACK
                                        setDrawValues(true)
                                        valueFormatter = DefaultValueFormatter(0)
                                    }
                                )
                                description.isEnabled = false
                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(
                                        qualityMap.keys.sorted().map { it.dayOfWeek.name.take(3) }
                                    )
                                }
                                legend.isEnabled = false
                            }
                        }
                    )
                }


                if (drinks.isNotEmpty()) ChartCard("Напитки по категориям") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            PieChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                setUsePercentValues(true)
                                setDrawHoleEnabled(false)
                                setDrawEntryLabels(false)
                                description.isEnabled = false
                                legend.isEnabled = true

                                val ds = PieDataSet(
                                    drinks.map { PieEntry(it.value.toFloat(), it.key) }, ""
                                ).apply {
                                    sliceSpace = 2f
                                    setColors(*ColorTemplate.MATERIAL_COLORS)
                                    valueFormatter = PercentFormatter()
                                    valueTextSize = 14f
                                    setDrawValues(true)
                                }
                                data = PieData(ds)
                            }
                        }
                    )
                }


                if (workoutsMap.isNotEmpty()) ChartCard("Тренировки по категориям") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            PieChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                setUsePercentValues(true)
                                setDrawHoleEnabled(false)
                                setDrawEntryLabels(false)
                                description.isEnabled = false
                                legend.isEnabled = true


                                val ds = PieDataSet(
                                    workoutsMap.map { PieEntry(it.value.toFloat(), it.key) }, ""
                                ).apply {
                                    sliceSpace = 2f
                                    setColors(*ColorTemplate.COLORFUL_COLORS)
                                    valueFormatter = PercentFormatter()
                                    valueTextSize = 14f
                                    setDrawValues(true)
                                }
                                data = PieData(ds)
                            }
                        }
                    )
                }


                if (meds.isNotEmpty()) ChartCard("Приём препаратов по дням") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            BarChart(ctx).apply {
                                val days = meds.keys.sorted()
                                val medNames = meds.values.flatten().distinct()
                                val dataSets = medNames.mapIndexed { idx, name ->
                                    val entries = days.mapIndexed { di, date ->
                                        val count = meds[date]?.count { it == name } ?: 0
                                        BarEntry(di.toFloat(), count.toFloat())
                                    }
                                    BarDataSet(entries, name).apply {
                                        color = ColorTemplate.MATERIAL_COLORS[idx % ColorTemplate.MATERIAL_COLORS.size]
                                        valueTextSize = 14f
                                        valueTextColor = android.graphics.Color.BLACK
                                        setDrawValues(true)
                                        valueFormatter = DefaultValueFormatter(0)
                                    }
                                }
                                data = BarData(dataSets as List<IBarDataSet>).apply {
                                    val groupSpace = 0.3f
                                    val barSpace = 0.05f
                                    setBarWidth((1f - groupSpace) / medNames.size - barSpace)
                                    groupBars(0f, groupSpace, barSpace)
                                }
                                description.isEnabled = false
                                setFitBars(true)

                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(days.map { it.dayOfWeek.name.take(3) })
                                    setCenterAxisLabels(true)
                                }
                                axisLeft.axisMinimum = 0f
                                axisRight.isEnabled = false
                                legend.isEnabled = true
                            }
                        }
                    )
                }


                if (symptomsMap.isNotEmpty()) ChartCard("Симптомы по дням") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            BarChart(ctx).apply {
                                val days = symptomsMap.keys.sorted()
                                val types = symptomsMap.values.flatten().distinct()
                                val dataSets = types.mapIndexed { idx, typeName ->
                                    val entries = days.mapIndexed { di, date ->
                                        val count = symptomsMap[date]?.count { it == typeName } ?: 0
                                        BarEntry(di.toFloat(), count.toFloat())
                                    }
                                    BarDataSet(entries, typeName).apply {
                                        color = ColorTemplate.MATERIAL_COLORS[idx % ColorTemplate.MATERIAL_COLORS.size]
                                        valueTextSize = 14f
                                        valueTextColor = android.graphics.Color.BLACK
                                        setDrawValues(true)
                                        valueFormatter = DefaultValueFormatter(0)
                                    }
                                }
                                data = BarData(dataSets as List<IBarDataSet>).apply {
                                    val groupSpace = 0.3f
                                    val barSpace = 0.05f
                                    setBarWidth((1f - groupSpace) / types.size - barSpace)
                                    groupBars(0f, groupSpace, barSpace)
                                }
                                description.isEnabled = false
                                setFitBars(true)
                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(days.map { it.dayOfWeek.name.take(3) })
                                    setCenterAxisLabels(true)
                                }
                                axisLeft.axisMinimum = 0f
                                axisRight.isEnabled = false
                                legend.isEnabled = true
                            }
                        }
                    )
                }


                if (fluid.isNotEmpty()) ChartCard("Жидкость по дням, мл") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            LineChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                xAxis.setDrawGridLines(false)
                                axisLeft.setDrawGridLines(false)
                                axisRight.isEnabled = false

                                val days = fluid.keys.sorted()
                                val entries = days.mapIndexed { i, date -> Entry(i.toFloat(), fluid[date]?.toFloat() ?: 0f) }
                                data = LineData(LineDataSet(entries, "").apply {
                                    lineWidth = 2f
                                    setColors(*ColorTemplate.MATERIAL_COLORS)
                                    valueTextSize = 14f
                                    valueTextColor = android.graphics.Color.BLACK
                                    setDrawValues(true)
                                })
                                description.isEnabled = false
                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(days.map { it.dayOfWeek.name.take(3) })
                                }
                                legend.isEnabled = false
                            }
                        }
                    )
                }


                if (avgIntensity.isNotEmpty()) ChartCard("Интенсивность тренировок по категориям") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            BarChart(ctx).apply {
                                val cats = avgIntensity.keys.toList()
                                val entries = cats.mapIndexed { i, cat -> BarEntry(i.toFloat(), avgIntensity[cat]!!.toFloat()) }
                                val ds = BarDataSet(entries, "").apply {
                                    setColors(*ColorTemplate.MATERIAL_COLORS)
                                    valueTextSize = 14f
                                    valueTextColor = android.graphics.Color.BLACK
                                    setDrawValues(true)
                                    valueFormatter = DefaultValueFormatter(0)
                                }
                                data = BarData(ds)
                                description.isEnabled = false
                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(cats)
                                    textSize = 12f
                                }
                                axisLeft.axisMinimum = 0f
                                axisRight.isEnabled = false
                                legend.isEnabled = false
                                animateY(500, Easing.EaseInOutCubic)
                            }
                        }
                    )
                }


                if (symptomIntensity.isNotEmpty()) ChartCard("Интенсивность симптомов по категориям") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            PieChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                setUsePercentValues(true)
                                setDrawHoleEnabled(false)
                                setDrawEntryLabels(false)
                                description.isEnabled = false
                                legend.isEnabled = true

                                val entries = symptomIntensity.map { PieEntry(it.value.toFloat(), it.key) }
                                val ds = PieDataSet(entries, "").apply {
                                    sliceSpace = 2f
                                    setColors(*ColorTemplate.MATERIAL_COLORS)
                                    valueFormatter = PercentFormatter()
                                    valueTextSize = 14f
                                    setDrawValues(true)
                                }
                                data = PieData(ds)
                            }
                        }
                    )
                }


                if (fastingByDay.isNotEmpty()) ChartCard("Самый длительный период голодания, ч") {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp),
                        factory = { ctx ->
                            BarChart(ctx).apply {
                                animateY(600, Easing.EaseInOutCubic)
                                val days = fastingByDay.keys.sorted()
                                val entries = days.mapIndexed { i, d -> BarEntry(i.toFloat(), fastingByDay[d]!!.toFloat()) }
                                val ds = BarDataSet(entries, "").apply {
                                    color = ColorTemplate.MATERIAL_COLORS[0]
                                    valueTextSize = 14f
                                    valueTextColor = android.graphics.Color.BLACK
                                    setDrawValues(true)
                                    valueFormatter = DefaultValueFormatter(0)
                                }
                                data = BarData(ds)

                                description.isEnabled = false
                                xAxis.apply {
                                    granularity = 1f
                                    valueFormatter = IndexAxisValueFormatter(days.map { it.dayOfWeek.name.take(3) })
                                    setDrawGridLines(false)
                                }
                                axisLeft.apply { axisMinimum = 0f; setDrawGridLines(false) }
                                axisRight.isEnabled = false
                                legend.isEnabled = false
                            }
                        }
                    )
                }
            }
        }
    }
}
