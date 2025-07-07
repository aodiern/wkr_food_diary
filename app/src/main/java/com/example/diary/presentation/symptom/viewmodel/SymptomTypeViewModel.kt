package com.example.diary.presentation.symptom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.symptom.SymptomType
import com.example.diary.domain.usecase.symptom.AddSymptomTypeUseCase
import com.example.diary.domain.usecase.symptom.DeleteSymptomTypeUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomTypesUseCase
import com.example.diary.domain.usecase.symptom.UpdateSymptomTypeUseCase
import com.example.diary.presentation.symptom.model.SymptomTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymptomTypeViewModel @Inject constructor(
    private val getAllSymptomTypesUseCase: GetAllSymptomTypesUseCase,
    private val addSymptomTypeUseCase: AddSymptomTypeUseCase,
    private val updateSymptomTypeUseCase: UpdateSymptomTypeUseCase,
    private val deleteSymptomTypeUseCase: DeleteSymptomTypeUseCase
) : ViewModel() {
    private val _types = MutableStateFlow<List<SymptomTypeUiModel>>(emptyList())
    val types: StateFlow<List<SymptomTypeUiModel>> = _types

    init {
        viewModelScope.launch {
            getAllSymptomTypesUseCase()
                .map { list -> list.map { SymptomTypeUiModel(it.typeId, it.name) } }
                .collect { _types.value = it }
        }
    }

    fun addType(name: String) = viewModelScope.launch {
        require(name.isNotBlank()) { "Название не может быть пустым" }
        addSymptomTypeUseCase(SymptomType(0, name))
    }

    fun editType(typeId: Long, newName: String) = viewModelScope.launch {
        require(newName.isNotBlank()) { "Название не может быть пустым" }
        updateSymptomTypeUseCase(SymptomType(typeId, newName))
    }

    fun removeType(typeId: Long) = viewModelScope.launch {
        deleteSymptomTypeUseCase(SymptomType(typeId, ""))
    }
}