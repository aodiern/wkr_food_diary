package com.example.diary.presentation.meal.screen

import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.presentation.meal.viewmodel.MealViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddMealScreen(
    viewModel: MealViewModel = hiltViewModel(),
    methods: List<CookingMethod>,
    ingredientsFromDb: List<Ingredient>,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    var dateTime by remember { mutableStateOf(LocalDateTime.now()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val timePicker = remember(context) {
        TimePickerDialog(context,
            { _: TimePicker, h, m -> dateTime = dateTime.withHour(h).withMinute(m) },
            dateTime.hour, dateTime.minute, true
        )
    }
    val datePicker = remember(context) {
        DatePickerDialog(context,
            { _: DatePicker, y, mo, d ->
                dateTime = dateTime.withYear(y).withMonth(mo+1).withDayOfMonth(d)
                timePicker.show()
            },
            dateTime.year, dateTime.monthValue-1, dateTime.dayOfMonth
        )
    }

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }


    var selectedMethodId by remember { mutableLongStateOf(0L) }
    var methodError by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(methods) {
        if (methods.isNotEmpty() && !methods.any { it.methodId == selectedMethodId }) {
            selectedMethodId = methods.first().methodId
        }
    }


    var newIngredientText by remember { mutableStateOf("") }
    val newIngredientNames = remember { mutableStateListOf<String>() }
    var duplicateWarning by remember { mutableStateOf<String?>(null) }


    val selectedExistingIngredientIds = remember { mutableStateListOf<Long>() }
    var ingredientToEdit by remember { mutableStateOf<Ingredient?>(null) }
    var editIngredientName by remember { mutableStateOf("") }
    var editExistingWarning by remember { mutableStateOf<String?>(null) }
    var ingredientToDelete by remember { mutableStateOf<Ingredient?>(null) }


    var newIngIndexToEdit by remember { mutableStateOf<Int?>(null) }
    var newIngEditText by remember { mutableStateOf("") }
    var newIngEditWarning by remember { mutableStateOf<String?>(null) }
    var newIngIndexToDelete by remember { mutableStateOf<Int?>(null) }


    var isMethodExpanded by remember { mutableStateOf(false) }
    var isExistingExpanded by remember { mutableStateOf(false) }


    var searchExistingText by remember { mutableStateOf("") }





    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
        text = "Добавить прием пищи",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = null
            },
            label = { Text("Название блюда") },
            isError = nameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError != null) {
            Text(
                text = nameError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isMethodExpanded = !isMethodExpanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Способ приготовления",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isMethodExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isMethodExpanded) "Свернуть" else "Развернуть"
            )
        }
        if (isMethodExpanded) {
            if (methods.isEmpty()) {
                Text(
                    text = "Нет доступных способов приготовления",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            methods.forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 4.dp)
                ) {
                    RadioButton(
                        selected = (method.methodId == selectedMethodId),
                        onClick = { selectedMethodId = method.methodId }
                    )
                    Text(method.methodName)
                }
            }
            if (methodError != null) {
                Text(
                    text = methodError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Новые ингредиенты:")
        OutlinedTextField(
            value = newIngredientText,
            onValueChange = {
                newIngredientText = it
                duplicateWarning = null
            },
            label = { Text("Введите новый ингредиент") },
            modifier = Modifier.fillMaxWidth()
        )
        if (duplicateWarning != null) {
            Text(
                text = duplicateWarning!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
        Button(
            onClick = {
                val trimmed = newIngredientText.trim()
                val existsInNew = newIngredientNames.any { it.equals(trimmed, ignoreCase = true) }
                val existsInDb = ingredientsFromDb.any { it.name.equals(trimmed, ignoreCase = true) }
                when {
                    trimmed.isBlank() -> {
                        duplicateWarning = "Имя не может быть пустым"
                    }
                    existsInNew || existsInDb -> {
                        duplicateWarning = "Ингредиент уже существует"
                    }
                    else -> {
                        newIngredientNames.add(trimmed)
                        newIngredientText = ""
                        duplicateWarning = null
                    }
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.End)
        ) {
            Text("Добавить")
        }

        Spacer(modifier = Modifier.height(8.dp))


        if (newIngredientNames.isNotEmpty()) {
            Text("Набранные новые ингредиенты:")
            newIngredientNames.forEachIndexed { index, ingName ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = ingName,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = {
                            newIngIndexToEdit = index
                            newIngEditText = newIngredientNames[index]
                            newIngEditWarning = null
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Редактировать новый ингредиент"
                        )
                    }
                    IconButton(
                        onClick = {
                            newIngIndexToDelete = index
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить новый ингредиент"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExistingExpanded = !isExistingExpanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Существующие ингредиенты",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExistingExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExistingExpanded) "Свернуть" else "Развернуть"
            )
        }
        if (isExistingExpanded) {

            OutlinedTextField(
                value = searchExistingText,
                onValueChange = { searchExistingText = it },
                label = { Text("Поиск по существующим") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            val filtered = ingredientsFromDb.filter {
                it.name.contains(searchExistingText.trim(), ignoreCase = true)
            }
            if (filtered.isEmpty()) {
                Text(
                    text = "Совпадений не найдено",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
            filtered.forEach { ingredient ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    val isChecked = ingredient.ingredientId in selectedExistingIngredientIds
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) selectedExistingIngredientIds.add(ingredient.ingredientId)
                            else selectedExistingIngredientIds.remove(ingredient.ingredientId)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ingredient.name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        onClick = {
                            ingredientToEdit = ingredient
                            editIngredientName = ingredient.name
                            editExistingWarning = null
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Редактировать существующий ингредиент"
                        )
                    }

                    IconButton(
                        onClick = {
                            ingredientToDelete = ingredient
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить существующий ингредиент"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = dateTime.format(formatter),
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата и время приёма") },
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.clickable { datePicker.show() }
                )
            },
            modifier = Modifier.fillMaxWidth().clickable { datePicker.show() }
        )



        Spacer(Modifier.height(16.dp))


        Button(
            onClick = {

                if (name.trim().isEmpty()) {
                    nameError = "Название не может быть пустым"
                    return@Button
                }

                if (methods.isNotEmpty() && !methods.any { it.methodId == selectedMethodId }) {
                    methodError = "Выберите способ приготовления"
                    return@Button
                }

                if (newIngredientNames.isEmpty() && selectedExistingIngredientIds.isEmpty()) {

                    return@Button
                }

                viewModel.addMealWithBoth(
                    name = name.trim(),
                    methodId = selectedMethodId,
                    newIngredientNames = newIngredientNames.toList(),
                    existingIngredientIds = selectedExistingIngredientIds.toList(),
                    dateTime = dateTime
                )
                onSaved()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Сохранить блюдо")
        }
    }




    if (newIngIndexToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                newIngIndexToEdit = null
                newIngEditText = ""
                newIngEditWarning = null
            },
            title = {
                Text(text = "Редактировать новый ингредиент")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newIngEditText,
                        onValueChange = {
                            newIngEditText = it
                            newIngEditWarning = null
                        },
                        label = { Text("Новое имя") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (newIngEditWarning != null) {
                        Text(
                            text = newIngEditWarning!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val idx = newIngIndexToEdit!!
                        val trimmed = newIngEditText.trim()
                        val existsInNew = newIngredientNames.anyIndexed { index2, name2 ->
                            index2 != idx && name2.equals(trimmed, ignoreCase = true)
                        }
                        val existsInDb = ingredientsFromDb.any { it.name.equals(trimmed, ignoreCase = true) }
                        when {
                            trimmed.isBlank() -> {
                                newIngEditWarning = "Имя не может быть пустым"
                            }
                            existsInNew || existsInDb -> {
                                newIngEditWarning = "Ингредиент уже существует"
                            }
                            else -> {
                                newIngredientNames[idx] = trimmed
                                newIngIndexToEdit = null
                                newIngEditText = ""
                                newIngEditWarning = null
                            }
                        }
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        newIngIndexToEdit = null
                        newIngEditText = ""
                        newIngEditWarning = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }




    if (newIngIndexToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                newIngIndexToDelete = null
            },
            title = {
                Text(text = "Удалить новый ингредиент")
            },
            text = {
                val idx = newIngIndexToDelete!!
                Text("Удалить «${newIngredientNames[idx]}» из списка новых ингредиентов?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val idx = newIngIndexToDelete!!
                        newIngredientNames.removeAt(idx)
                        newIngIndexToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        newIngIndexToDelete = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }




    if (ingredientToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                ingredientToEdit = null
                editIngredientName = ""
                editExistingWarning = null
            },
            title = {
                Text(text = "Редактировать ингредиент")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editIngredientName,
                        onValueChange = {
                            editIngredientName = it
                            editExistingWarning = null
                        },
                        label = { Text("Новое имя") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (editExistingWarning != null) {
                        Text(
                            text = editExistingWarning!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val toEdit = ingredientToEdit!!
                        val trimmed = editIngredientName.trim()
                        val existsInDb = ingredientsFromDb.any {
                            it.name.equals(trimmed, ignoreCase = true) && it.ingredientId != toEdit.ingredientId
                        }
                        val existsInNew = newIngredientNames.any { it.equals(trimmed, ignoreCase = true) }
                        when {
                            trimmed.isBlank() -> {
                                editExistingWarning = "Имя не может быть пустым"
                            }
                            existsInDb || existsInNew -> {
                                editExistingWarning = "Ингредиент уже существует"
                            }
                            else -> {
                                viewModel.editIngredient(toEdit.ingredientId, trimmed)
                                ingredientToEdit = null
                                editIngredientName = ""
                                editExistingWarning = null
                            }
                        }
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        ingredientToEdit = null
                        editIngredientName = ""
                        editExistingWarning = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }




    if (ingredientToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                ingredientToDelete = null
            },
            title = {
                Text(text = "Удалить ингредиент")
            },
            text = {
                Text("Вы уверены, что хотите удалить «${ingredientToDelete?.name}» из базы?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val toDelete = ingredientToDelete!!
                        viewModel.removeIngredient(toDelete.ingredientId)

                        selectedExistingIngredientIds.remove(toDelete.ingredientId)
                        ingredientToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        ingredientToDelete = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}


private inline fun List<String>.anyIndexed(predicate: (Int, String) -> Boolean): Boolean {
    for (i in indices) {
        if (predicate(i, this[i])) return true
    }
    return false
}
