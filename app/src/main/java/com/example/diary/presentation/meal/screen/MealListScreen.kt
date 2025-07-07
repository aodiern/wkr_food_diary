package com.example.diary.presentation.meal.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.diary.presentation.meal.model.MealUiModel
import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.data.local.entity.meal.Ingredient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MealListScreen(
//    viewModel: MealViewModel = hiltViewModel(),
//    onNavigateToAdd: () -> Unit
//) {
//    val meals by viewModel.meals.collectAsState()
//    val methods by viewModel.methods.collectAsState()  // List<CookingMethod>
//    val ingredients by viewModel.ingredients.collectAsState()
//
//    var mealToEdit by remember { mutableStateOf<MealUiModel?>(null) }
//    var mealToDelete by remember { mutableStateOf<MealUiModel?>(null) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Приёмы пищи") },
//                actions = {
//                    IconButton(onClick = onNavigateToAdd) {
//                        Icon(Icons.Default.Edit, contentDescription = "Добавить")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        LazyColumn(
//            contentPadding = padding,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//        ) {
//            items(meals) { meal ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    elevation = CardDefaults.cardElevation(4.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(12.dp)
//                    ) {
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(meal.name, style = MaterialTheme.typography.titleMedium)
//                            Text("Способ: ${meal.method}", style = MaterialTheme.typography.bodySmall)
//                            if (meal.ingredients.isNotEmpty()) {
//                                Text(
//                                    text = "Ингредиенты: ${meal.ingredients.joinToString(", ")}",
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            }
//                        }
//                        IconButton(onClick = { mealToEdit = meal }) {
//                            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
//                        }
//                        IconButton(onClick = { mealToDelete = meal }) {
//                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    // Диалог редактирования
//    mealToEdit?.let { meal ->
//        MealEditDialog(
//            meal = meal,
//            methods = methods,
//            ingredients = ingredients,
//            onDismiss = { mealToEdit = null },
//            onSave = { name, methodId, ingredientIds ->
//                viewModel.editMeal(meal.mealId, name, methodId, ingredientIds)
//                mealToEdit = null
//            }
//        )
//    }
//
//    // Диалог удаления
//    if (mealToDelete != null) {
//        AlertDialog(
//            onDismissRequest = { mealToDelete = null },
//            title = { Text("Удалить приём пищи") },
//            text = { Text("Вы уверены, что хотите удалить «${mealToDelete!!.name}»?") },
//            confirmButton = {
//                TextButton(onClick = {
//                    viewModel.removeMeal(mealToDelete!!.mealId)
//                    mealToDelete = null
//                }) {
//                    Text("Удалить")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { mealToDelete = null }) {
//                    Text("Отмена")
//                }
//            }
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealEditDialog(
    meal: MealUiModel,
    methods: List<CookingMethod>,
    ingredients: List<Ingredient>,
    onDismiss: () -> Unit,
    onSave: (name: String, methodId: Long, ingredientIds: List<Long>, dateTime: LocalDateTime) -> Unit
) {

    var name by remember { mutableStateOf(meal.name) }
    var methodId by remember { mutableLongStateOf(meal.cookingMethodId) }
    val ingredientIds = remember { mutableStateListOf<Long>() }
    var dateTime by remember { mutableStateOf(meal.dateTime) }
    val selectedIds = remember { mutableStateListOf<Long>() }



    LaunchedEffect(meal.mealId) {
        ingredientIds.clear()
        ingredientIds.addAll(meal.ingredientIds)
    }
    LaunchedEffect(meal.mealId) {
        selectedIds.clear()
        selectedIds.addAll(meal.ingredientIds)
    }

    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    val datePicker = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year, month, day ->
                dateTime = dateTime
                    .withYear(year)
                    .withMonth(month + 1)
                    .withDayOfMonth(day)
            },
            dateTime.year,
            dateTime.monthValue - 1,
            dateTime.dayOfMonth
        )
    }

    val timePicker = remember {
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать приём пищи") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название блюда") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))


                Text("Способ приготовления:", style = MaterialTheme.typography.bodySmall)
                methods.forEach { m ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { methodId = m.methodId }
                            .padding(start = 8.dp, top = 4.dp)
                    ) {
                        RadioButton(
                            selected = m.methodId == methodId,
                            onClick = { methodId = m.methodId }
                        )
                        Text(m.methodName, Modifier.padding(start = 4.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))


                Text("Ингредиенты:", style = MaterialTheme.typography.bodySmall)
                ingredients.forEach { ing ->
                    val checked = ing.ingredientId in ingredientIds
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (checked) ingredientIds.remove(ing.ingredientId)
                                else ingredientIds.add(ing.ingredientId)
                            }
                            .padding(start = 8.dp, top = 4.dp)
                    ) {
                        Checkbox(checked = checked, onCheckedChange = null)
                        Text(ing.name, Modifier.padding(start = 4.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))


                OutlinedTextField(
                    value = dateTime.format(formatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата и время") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                datePicker.show()
                                timePicker.show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            datePicker.show()
                            timePicker.show()
                        }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val trimmed = name.trim()
                if (trimmed.isNotEmpty()) {
                    onSave(trimmed, methodId, ingredientIds.toList(), dateTime)
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