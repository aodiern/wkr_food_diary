package com.example.diary.domain.model.medication

import java.time.LocalDate

data class MedicationDefinition(
    val id: Long,
    val name: String,
    val form: String? = null,
    val strength: String? = null,
    val manufacturer: String? = null,
    val packageSize: String? = null,
    val barcode: String? = null,
    val description: String? = null,
    val expiryDate: LocalDate? = null
)