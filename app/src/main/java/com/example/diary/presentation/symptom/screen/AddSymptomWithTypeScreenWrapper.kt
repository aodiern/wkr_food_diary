package com.example.diary.presentation.symptom.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.diary.presentation.symptom.viewmodel.SymptomTypeViewModel
import com.example.diary.presentation.symptom.viewmodel.SymptomViewModel

@Composable
fun AddSymptomWithTypeScreenWrapper(
    navController: NavController,
    symptomTypeViewModel: SymptomTypeViewModel = hiltViewModel(),
    symptomViewModel: SymptomViewModel = hiltViewModel()
) {
    AddSymptomWithTypeScreen(
        symptomTypeViewModel = symptomTypeViewModel,
        symptomViewModel     = symptomViewModel,
        onSaved = { navController.popBackStack() }
    )
}
