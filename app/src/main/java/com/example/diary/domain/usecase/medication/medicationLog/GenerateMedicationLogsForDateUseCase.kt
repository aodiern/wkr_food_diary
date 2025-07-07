package com.example.diary.domain.usecase.medication.medicationLog

import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.domain.repository.medication.MedicationLogRepository
import com.example.diary.domain.repository.medication.MedicationPlanRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime


class GenerateMedicationLogsForDateUseCase(
    private val planRepository: MedicationPlanRepository,
    private val logRepository: MedicationLogRepository
) {
    suspend operator fun invoke(date: LocalDate) {

        val existing: List<MedicationLog> = logRepository
            .getLogsForDate(date)
            .first()


        val plans: List<MedicationPlan> = planRepository
            .getAllPlans()
            .first()


        for (plan in plans) {
            for (time in plan.times) {
                val dateTime = LocalDateTime.of(date, time)
                val alreadyExists = existing.any {
                    it.planId == plan.id && it.scheduledTime == dateTime
                }
                if (!alreadyExists) {
                    logRepository.addLog(
                        MedicationLog(
                            id = 0L,
                            planId = plan.id,
                            name = "",
                            dose = "",
                            scheduledTime = dateTime,
                            taken = false
                        )
                    )
                }
            }
        }
    }
}