package com.example.diary.presentation.symptom.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.symptom.viewmodel.SymptomTypeViewModel

@Composable
fun AddSymptomTypeScreenWrapper(
    viewModel: SymptomTypeViewModel = hiltViewModel(),
    onSaved: () -> Unit
) {
    AddSymptomTypeScreen(
        viewModel = viewModel,
        onSaved = onSaved
    )
}