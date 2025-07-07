package com.example.diary.data.local.entity.water

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.diary.data.local.converter.Converters
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "water_intake")
@TypeConverters(Converters::class)
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true) val intakeId: Long = 0L,
    val amountMl: Int,
    val categoryId: Long,
    val timestamp: LocalDateTime
)

data class WaterIntakeStatsEntity(val date: LocalDate, val totalAmountMl: Int, val intakeCount: Int)
data class WaterCategoryStatsEntity(val category: String, val totalAmountMl: Int)