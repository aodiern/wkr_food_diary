package com.example.diary.presentation.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.workout.WorkoutSession
import com.example.diary.domain.model.workout.WorkoutType
import com.example.diary.domain.usecase.AddWorkoutSessionUseCase
import com.example.diary.domain.usecase.AddWorkoutTypeUseCase
import com.example.diary.domain.usecase.DeleteWorkoutSessionUseCase
import com.example.diary.domain.usecase.DeleteWorkoutTypeUseCase
import com.example.diary.domain.usecase.GetAllWorkoutTypesUseCase
import com.example.diary.domain.usecase.GetWorkoutSessionsForDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getAllTypesUseCase: GetAllWorkoutTypesUseCase,
    private val addTypeUseCase: AddWorkoutTypeUseCase,
    private val deleteTypeUseCase: DeleteWorkoutTypeUseCase,
    private val getSessionsUseCase: GetWorkoutSessionsForDateUseCase,
    private val addSessionUseCase: AddWorkoutSessionUseCase,
    private val deleteSessionUseCase: DeleteWorkoutSessionUseCase
) : ViewModel() {

    private val _types = MutableStateFlow<List<WorkoutType>>(emptyList())
    val types: StateFlow<List<WorkoutType>> = _types

    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val sessions: StateFlow<List<WorkoutSession>> = _sessions

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    init {

        viewModelScope.launch {
            getAllTypesUseCase()
                .collect { list -> _types.value = list }
        }

        loadSessions(LocalDate.now())
    }


    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadSessions(date)
    }


    fun loadSessions(date: LocalDate) = viewModelScope.launch {
        getSessionsUseCase(date)
            .collect { list -> _sessions.value = list }
    }


    fun addType(name: String) = viewModelScope.launch {
        if (name.isNotBlank()) {
            addTypeUseCase(WorkoutType(0L, name.trim()))
            _types.value = getAllTypesUseCase().firstOrNull().orEmpty()
        }
    }


    fun deleteType(type: WorkoutType) = viewModelScope.launch {
        deleteTypeUseCase(type)
        _types.value = getAllTypesUseCase().firstOrNull().orEmpty()
    }


    fun addSession(session: WorkoutSession) = viewModelScope.launch {
        addSessionUseCase(session)
        loadSessions(_selectedDate.value)
    }


    fun deleteSession(id: Long) = viewModelScope.launch {
        deleteSessionUseCase(id)
        loadSessions(_selectedDate.value)
    }
}
