package com.example.diary.data.mapper


import com.example.diary.data.local.entity.SleepSessionEntity
import com.example.diary.data.local.entity.StressLogEntity
import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.data.local.entity.meal.MealWithIngredientsAndMethod
import com.example.diary.data.local.entity.medication.MedicationDefinitionEntity
import com.example.diary.data.local.entity.medication.MedicationLogEntity
import com.example.diary.data.local.entity.medication.MedicationPlanEntity
import com.example.diary.data.local.entity.symptom.SymptomEntity
import com.example.diary.data.local.entity.symptom.SymptomTypeEntity
import com.example.diary.data.local.entity.user.UserProfileEntity
import com.example.diary.data.local.entity.water.BeverageCategoryEntity
import com.example.diary.data.local.entity.water.WaterIntakeEntity
import com.example.diary.data.local.entity.workout.WorkoutSessionEntity
import com.example.diary.data.local.entity.workout.WorkoutTypeEntity
import com.example.diary.domain.model.*
import com.example.diary.presentation.meal.model.MealUiModel
import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.model.symptom.SymptomType
import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.domain.model.user.UserProfileModel
import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.model.workout.WorkoutSession
import com.example.diary.domain.model.workout.WorkoutType
import com.example.diary.presentation.symptom.model.SymptomUiModel

fun MealWithIngredientsAndMethod.toDomain(): MealDetails = MealDetails(
    meal = Meal(
        mealId = meal.mealId,
        name = meal.name,
        cookingMethodId = meal.cookingMethodId,
        dateTime = meal.dateTime
    ),
    ingredients = ingredients.map { Ingredient(it.ingredientId, it.name) },
    method = CookingMethod(
        methodId = cookingMethod.methodId,
        methodName = cookingMethod.methodName
    )
)


//fun MealDetails.toUi(): MealUiModel =
//    MealUiModel(
//        mealId = this.meal.mealId,
//        cookingMethodId = this.meal.cookingMethodId,
//        name = this.meal.name,
//        method = this.method.methodName,
//        ingredients = this.ingredients.map { it.name }
//    )
fun MealDetails.toUi(): MealUiModel =
    MealUiModel(
        mealId         = meal.mealId,
        cookingMethodId= meal.cookingMethodId,
        name           = meal.name,
        method         = method.methodName,
        ingredients    = ingredients.map { it.name },
        ingredientIds  = ingredients.map { it.ingredientId },
        dateTime = meal.dateTime
    )

fun SymptomEntity.toDomain(): Symptom =
    Symptom(symptomId, typeId, intensity, dateTime)

fun Symptom.toEntity(): SymptomEntity =
    SymptomEntity(symptomId, typeId, intensity, dateTime)

fun SymptomTypeEntity.toDomain(): SymptomType =
    SymptomType(typeId, name)

fun SymptomType.toEntity(): SymptomTypeEntity =
    SymptomTypeEntity(typeId, name)

fun Symptom.toUi(typeName: String = ""): SymptomUiModel = SymptomUiModel(
    symptomId = this.symptomId,
    typeId    = this.typeId,
    typeName  = typeName,
    intensity = this.intensity,
    dateTime  = this.dateTime
)

fun SymptomUiModel.toDomain(): Symptom = Symptom(
    symptomId = this.symptomId,
    typeId = this.typeId,
    intensity = this.intensity,
    dateTime = this.dateTime
)

fun MedicationDefinitionEntity.toDomain(): MedicationDefinition =
    MedicationDefinition(
        id = this.defId,
        name = this.name,
        form = this.form,
        strength = this.strength
    )

fun MedicationDefinition.toEntity(): MedicationDefinitionEntity =
    MedicationDefinitionEntity(
        defId = this.id,
        name = this.name,
        form = this.form,
        strength = this.strength
    )

fun MedicationPlanEntity.toDomain(): MedicationPlan =
    MedicationPlan(
        id = planId,
        definitionId = definitionId,
        dosage = dosage,
        timesPerDay = timesPerDay,
        times = times,
        startDate = startDate,
        endDate = endDate
    )

fun MedicationPlan.toEntity(): MedicationPlanEntity =
    MedicationPlanEntity(
        planId = id,
        definitionId = definitionId,
        dosage = dosage,
        timesPerDay = timesPerDay,
        times = times,
        startDate = startDate,
        endDate = endDate
    )

fun MedicationLogEntity.toDomain(definitionName: String? = null): MedicationLog {
    val name = this.adHocName ?: definitionName.orEmpty()
    val dose = this.adHocDose ?: ""
    return MedicationLog(
        id = this.logId,
        planId = this.planId,
        name = name,
        dose = dose,
        scheduledTime = this.scheduledTime,
        taken = this.taken
    )
}

fun MedicationLog.toEntity(): MedicationLogEntity =
    MedicationLogEntity(
        logId = this.id,
        planId = this.planId,
        adHocName = if (this.planId == null) this.name else null,
        adHocDose = if (this.planId == null) this.dose else null,
        scheduledTime = this.scheduledTime,
        taken = this.taken
    )

fun BeverageCategoryEntity.toDomain(): BeverageCategory =
    BeverageCategory(
        categoryId = this.categoryId,
        name = this.name
    )

fun BeverageCategory.toEntity(): BeverageCategoryEntity =
    BeverageCategoryEntity(
        categoryId = this.categoryId,
        name = this.name
    )

fun WaterIntakeEntity.toDomain(): WaterIntake =
    WaterIntake(
        intakeId = this.intakeId,
        amountMl = this.amountMl,
        categoryId = this.categoryId,
        timestamp = this.timestamp
    )

fun WaterIntake.toEntity(): WaterIntakeEntity =
    WaterIntakeEntity(
        intakeId = this.intakeId,
        amountMl = this.amountMl,
        categoryId = this.categoryId,
        timestamp = this.timestamp
    )

fun SleepSessionEntity.toDomain(): SleepSession =
    SleepSession(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        quality = quality
    )

fun SleepSession.toEntity(): SleepSessionEntity =
    SleepSessionEntity(
        sessionId = sessionId,
        startTime = startTime,
        endTime = endTime,
        quality = quality
    )

fun WorkoutTypeEntity.toDomain(): WorkoutType =
    WorkoutType(
        typeId = this.typeId,
        name = this.name
    )


fun WorkoutType.toEntity(): WorkoutTypeEntity =
    WorkoutTypeEntity(
        typeId = this.typeId,
        name = this.name
    )


fun WorkoutSessionEntity.toDomain(): WorkoutSession =
    WorkoutSession(
        sessionId = this.sessionId,
        workoutTypeId = this.workoutTypeId,
        startTime = this.startTime,
        endTime = this.endTime,
        intensity = this.intensity
    )


fun WorkoutSession.toEntity(): WorkoutSessionEntity =
    WorkoutSessionEntity(
        sessionId = this.sessionId,
        workoutTypeId = this.workoutTypeId,
        startTime = this.startTime,
        endTime = this.endTime,
        intensity = this.intensity
    )

fun StressLogEntity.toDomain(): StressLog =
    StressLog(
        logId = this.logId,
        level = this.level,
        timestamp = this.timestamp
    )


fun StressLog.toEntity(): StressLogEntity =
    StressLogEntity(
        logId = this.logId,
        level = this.level,
        timestamp = this.timestamp
    )


fun UserProfileEntity.toModel(): UserProfileModel = UserProfileModel(
    fio = this.fio,
    dob = this.dob,
    diagnoses = this.diagnoses,
    passwordHash = this.passwordHash,
    theme = this.theme
)


fun UserProfileModel.toEntity(): UserProfileEntity = UserProfileEntity(
    id = 0,
    fio = this.fio,
    dob = this.dob,
    diagnoses = this.diagnoses,
    passwordHash = this.passwordHash,
    theme = this.theme
)



