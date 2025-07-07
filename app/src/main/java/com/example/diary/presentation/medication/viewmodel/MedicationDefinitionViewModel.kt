package com.example.diary.presentation.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.domain.usecase.medication.medicationDefinition.AddMedicationDefinitionUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.DeleteMedicationDefinitionUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.GetAllMedicationDefinitionsUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.UpdateMedicationDefinitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MedicationDefinitionViewModel @Inject constructor(
    private val getAllDefs: GetAllMedicationDefinitionsUseCase,
    private val addDef: AddMedicationDefinitionUseCase,
    private val updateDef: UpdateMedicationDefinitionUseCase,
    private val deleteDef: DeleteMedicationDefinitionUseCase
) : ViewModel() {
    private val _definitions = MutableStateFlow<List<MedicationDefinition>>(emptyList())
    val definitions: StateFlow<List<MedicationDefinition>> = _definitions

    init {
        loadDefinitions()
    }

    fun loadDefinitions() = viewModelScope.launch {
        val list = getAllDefs()
        _definitions.value = list
    }

    fun createDefinition(def: MedicationDefinition) = viewModelScope.launch {
        addDef(def)
        loadDefinitions()
    }

    fun editDefinition(def: MedicationDefinition) = viewModelScope.launch {
        updateDef(def)
        loadDefinitions()
    }

    fun removeDefinition(id: Long) = viewModelScope.launch {
        deleteDef(id)
        loadDefinitions()
    }
}
