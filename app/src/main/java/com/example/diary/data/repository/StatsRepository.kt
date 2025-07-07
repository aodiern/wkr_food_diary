package com.example.diary.data.repository

import com.example.diary.data.local.dao.SleepSessionDao
import com.example.diary.data.local.dao.StressLogDao
import com.example.diary.data.local.dao.symptom.SymptomDao
import com.example.diary.data.local.dao.water.WaterIntakeDao
import com.example.diary.domain.model.stats.DailySleepStat
import com.example.diary.domain.model.stats.DailyWaterStat
import com.example.diary.domain.model.stats.WaterCategoryStat
import com.example.diary.domain.model.stats.DailyStressStat
import com.example.diary.domain.model.stats.SymptomFrequency
import com.example.diary.domain.model.stats.SymptomIntensity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class StatsRepository(
    private val sleepDao: SleepSessionDao,
    private val waterDao: WaterIntakeDao,
    private val stressDao: StressLogDao,
    private val symptomDao: SymptomDao
) {

    
    fun getDailySleepStats(start: LocalDate, end: LocalDate): Flow<List<DailySleepStat>> =
        sleepDao.getDailyStats(start, end)
            .map { list ->
                list.map { dto ->
                    DailySleepStat(
                        date = dto.date,
                        totalDurationHours = dto.totalDurationHours,
                        averageQuality = dto.averageQuality
                    )
                }
            }

    
    fun getDailyWaterStats(start: LocalDate, end: LocalDate): Flow<List<DailyWaterStat>> =
        waterDao.getDailyStats(start, end)
            .map { list ->
                list.map { dto ->
                    DailyWaterStat(
                        date = dto.date,
                        totalAmountMl = dto.totalAmountMl,
                        intakeCount = dto.intakeCount
                    )
                }
            }

    fun getWaterCategoryStats(start: LocalDate, end: LocalDate): Flow<List<WaterCategoryStat>> =
        waterDao.getCategoryStats(start, end)
            .map { list ->
                list.map { dto ->
                    WaterCategoryStat(
                        category = dto.category,
                        totalAmountMl = dto.totalAmountMl
                    )
                }
            }

    
    fun getDailyStressStats(start: LocalDate, end: LocalDate): Flow<List<DailyStressStat>> =
        stressDao.getDailyStats(start, end)
            .map { list ->
                list.map { dto ->
                    DailyStressStat(
                        date = dto.date,
                        averageLevel = dto.averageLevel,
                        measurementCount = dto.measurementCount
                    )
                }
            }

    
    fun getSymptomFrequency(
        start: LocalDate,
        end: LocalDate,
        limit: Int
    ): Flow<List<SymptomFrequency>> =
        symptomDao.getMostFrequent(start, end, limit)
            .map { list ->
                list.map { dto ->
                    SymptomFrequency(
                        name = dto.name,
                        count = dto.count
                    )
                }
            }

    fun getSymptomIntensity(
        start: LocalDate,
        end: LocalDate,
        limit: Int
    ): Flow<List<SymptomIntensity>> =
        symptomDao.getHighestIntensity(start, end, limit)
            .map { list ->
                list.map { dto ->
                    SymptomIntensity(
                        name = dto.name,
                        averageIntensity = dto.avgIntensity
                    )
                }
            }
}
