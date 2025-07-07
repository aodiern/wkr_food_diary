package com.example.diary.presentation.symptom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.usecase.symptom.AddSymptomUseCase
import com.example.diary.domain.usecase.symptom.DeleteSymptomUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomsUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomTypesUseCase
import com.example.diary.domain.usecase.symptom.UpdateSymptomUseCase
import com.example.diary.presentation.symptom.model.SymptomTypeUiModel
import com.example.diary.presentation.symptom.model.SymptomUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SymptomViewModel @Inject constructor(
    private val getAllSymptomsUseCase: GetAllSymptomsUseCase,
    private val addSymptomUseCase: AddSymptomUseCase,
    private val updateSymptomUseCase: UpdateSymptomUseCase,
    private val deleteSymptomUseCase: DeleteSymptomUseCase,
    private val getAllSymptomTypesUseCase: GetAllSymptomTypesUseCase
) : ViewModel() {

    private val _symptoms = MutableStateFlow<List<SymptomUiModel>>(emptyList())
    val symptoms: StateFlow<List<SymptomUiModel>> = _symptoms

    private val _types = MutableStateFlow<List<SymptomTypeUiModel>>(emptyList())
    val types: StateFlow<List<SymptomTypeUiModel>> = _types

    init {

        viewModelScope.launch {
            getAllSymptomTypesUseCase()
                .map { list -> list.map { SymptomTypeUiModel(it.typeId, it.name) } }
                .collect { _types.value = it }
        }


        viewModelScope.launch {
            combine(
                getAllSymptomsUseCase(),
                getAllSymptomTypesUseCase()
            ) { symptomsDomain, typesDomain ->
                symptomsDomain.map { s ->
                    val typeName = typesDomain.firstOrNull { it.typeId == s.typeId }?.name
                        ?: "Неизвестный тип"
                    SymptomUiModel(
                        symptomId = s.symptomId,
                        typeId = s.typeId,
                        typeName = typeName,
                        intensity = s.intensity,
                        dateTime = s.dateTime
                    )
                }
            }
                .collect { uiList ->
                    _symptoms.value = uiList
                }
        }
    }

    fun addSymptom(typeId: Long, intensity: Int, dateTime: LocalDateTime) = viewModelScope.launch {
        require(intensity in 1..10) { "Интенсивность должна быть от 1 до 10" }
        addSymptomUseCase(Symptom(0, typeId, intensity, dateTime))
    }

    fun editSymptom(symptomId: Long, typeId: Long, intensity: Int, dateTime: LocalDateTime) = viewModelScope.launch {
        require(intensity in 1..10) { "Интенсивность должна быть от 1 до 10" }
        updateSymptomUseCase(Symptom(symptomId, typeId, intensity, dateTime))
    }

    fun removeSymptom(symptomId: Long) = viewModelScope.launch {
        deleteSymptomUseCase(Symptom(symptomId, 0, 0, LocalDateTime.now()))
    }
}
