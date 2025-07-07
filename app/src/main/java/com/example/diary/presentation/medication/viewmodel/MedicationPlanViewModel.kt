package com.example.diary.presentation.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.domain.usecase.medication.medicationPlan.AddMedicationPlanUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.DeleteMedicationPlanUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.GetAllMedicationPlansUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.UpdateMedicationPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MedicationPlanViewModel @Inject constructor(
    private val getAllPlans: GetAllMedicationPlansUseCase,
    private val addPlan: AddMedicationPlanUseCase,
    private val updatePlan: UpdateMedicationPlanUseCase,
    private val deletePlan: DeleteMedicationPlanUseCase
) : ViewModel() {
    private val _plans = MutableStateFlow<List<MedicationPlan>>(emptyList())
    val plans: StateFlow<List<MedicationPlan>> = _plans

    init {
        loadPlans()
    }

    fun loadPlans() = viewModelScope.launch {
        getAllPlans()
            .collect { list ->
                _plans.value = list
            }
    }

    fun createPlan(plan: MedicationPlan) = viewModelScope.launch {
        addPlan(plan)
        loadPlans()
    }

    fun editPlan(plan: MedicationPlan) = viewModelScope.launch {
        updatePlan(plan)
        loadPlans()
    }

    fun removePlan(id: Long) = viewModelScope.launch {
        deletePlan(id)
        loadPlans()
    }
}
