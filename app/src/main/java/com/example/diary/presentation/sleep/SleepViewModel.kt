package com.example.diary.presentation.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.SleepSession
import com.example.diary.domain.usecase.sleep.AddSleepSessionUseCase
import com.example.diary.domain.usecase.sleep.DeleteSleepSessionUseCase
import com.example.diary.domain.usecase.sleep.GetSleepSessionsForDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val getSessionsUseCase: GetSleepSessionsForDateUseCase,
    private val addSessionUseCase: AddSleepSessionUseCase,
    private val deleteSessionUseCase: DeleteSleepSessionUseCase
) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SleepSession>>(emptyList())
    val sessions: StateFlow<List<SleepSession>> = _sessions

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    init {
        loadSessions(LocalDate.now())
    }

    fun loadSessions(date: LocalDate) = viewModelScope.launch {
        _selectedDate.value = date
        getSessionsUseCase(date)
            .collect { list -> _sessions.value = list }
    }

    fun addSession(session: SleepSession) = viewModelScope.launch {
        addSessionUseCase(session)
        loadSessions(_selectedDate.value)
    }

    fun deleteSession(sessionId: Long) = viewModelScope.launch {
        deleteSessionUseCase(sessionId)
        loadSessions(_selectedDate.value)
    }
}
