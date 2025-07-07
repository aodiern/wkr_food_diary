package com.example.diary.presentation.stress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.StressLog
import com.example.diary.domain.repository.StressLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StressLogViewModel @Inject constructor(
    private val repository: StressLogRepository
) : ViewModel() {

    private val _logs = MutableStateFlow<List<StressLog>>(emptyList())
    val logs: StateFlow<List<StressLog>> = _logs

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    init {

        loadLogsForDate(_selectedDate.value)
    }


    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadLogsForDate(date)
    }


    fun loadLogsForDate(date: LocalDate) = viewModelScope.launch {
        repository.getLogsForDate(date)
            .collect { list -> _logs.value = list }
    }


    fun addLog(level: Int, timestamp: LocalDateTime) = viewModelScope.launch {
        if (level in 1..10) {
            repository.addLog(StressLog(0L, level, timestamp))
            loadLogsForDate(_selectedDate.value)
        }
    }


    fun deleteLog(logId: Long) = viewModelScope.launch {
        repository.deleteLog(logId)
        loadLogsForDate(_selectedDate.value)
    }
}
