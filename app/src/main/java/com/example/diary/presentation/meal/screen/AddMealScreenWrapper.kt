package com.example.diary.presentation.meal.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.meal.viewmodel.MealViewModel
import androidx.compose.runtime.getValue

@Composable
fun AddMealScreenWrapper(viewModel: MealViewModel = hiltViewModel(), onSaved: () -> Unit) {
    val methods by viewModel.methods.collectAsState()
    val ingredientsFromDb by viewModel.ingredients.collectAsState()

    AddMealScreen(
        viewModel = viewModel,
        methods = methods,
        ingredientsFromDb = ingredientsFromDb,
        onSaved = onSaved
    )
}
