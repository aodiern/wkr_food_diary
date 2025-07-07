package com.example.diary.presentation.symptom.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.diary.presentation.symptom.viewmodel.SymptomTypeViewModel

@Composable
fun SymptomTypeListScreenWrapper(
    viewModel: SymptomTypeViewModel = hiltViewModel(),
    onNavigateToAdd: () -> Unit
) {
    SymptomTypeListScreen(
        viewModel = viewModel,
        onNavigateToAdd = onNavigateToAdd
    )
}