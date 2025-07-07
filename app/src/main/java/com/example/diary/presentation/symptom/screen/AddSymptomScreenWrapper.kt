package com.example.diary.presentation.symptom.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.symptom.viewmodel.SymptomViewModel

@Composable
fun AddSymptomScreenWrapper(
    viewModel: SymptomViewModel = hiltViewModel(),
    onSaved: () -> Unit
) {
    AddSymptomScreen(
        viewModel = viewModel,
        onSaved = onSaved
    )
}