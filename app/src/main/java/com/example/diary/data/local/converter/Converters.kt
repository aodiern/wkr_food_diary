package com.example.diary.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Converters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    // LocalDate converters
    @TypeConverter
    @JvmStatic
    fun fromLocalDate(date: LocalDate?): String? = date?.format(dateFormatter)

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it, dateFormatter) }

    // LocalDateTime converters
    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.format(dateTimeFormatter)

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    // LocalTime converters
    @TypeConverter
    @JvmStatic
    fun fromLocalTime(time: LocalTime?): String? = time?.format(timeFormatter)

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?): LocalTime? = value?.let { LocalTime.parse(it, timeFormatter) }

    // List<LocalTime> converters (for MedicationPlanEntity.times)
    @TypeConverter
    @JvmStatic
    fun fromLocalTimeList(times: List<LocalTime>?): String? = times
        ?.joinToString(separator = ",") { it.format(timeFormatter) }

    @TypeConverter
    @JvmStatic
    fun toLocalTimeList(data: String?): List<LocalTime>? = data
        ?.split(",")
        ?.map { LocalTime.parse(it, timeFormatter) }
        ?: emptyList()
}