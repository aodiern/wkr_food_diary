package com.example.diary.domain.repository.medication

import com.example.diary.domain.model.medication.MedicationLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MedicationLogRepository {

    fun getLogsForDate(date: LocalDate): Flow<List<MedicationLog>>

    suspend fun addLog(log: MedicationLog): Long

    suspend fun updateLog(log: MedicationLog)

    suspend fun deleteLog(id: Long)

}