package com.example.diary.domain.usecase.medication.medicationLog

import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.repository.medication.MedicationLogRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetMedicationLogsForDateUseCase(
    private val repository: MedicationLogRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<MedicationLog>> =
        repository.getLogsForDate(date)
}