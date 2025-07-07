package com.example.diary.domain.usecase

import com.example.diary.data.local.db.AppDatabase
import com.example.diary.data.local.entity.SleepSessionEntity
import com.example.diary.data.local.entity.StressLogEntity
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.data.local.entity.meal.*
import com.example.diary.data.local.entity.medication.MedicationDefinitionEntity
import com.example.diary.data.local.entity.medication.MedicationLogEntity
import com.example.diary.data.local.entity.medication.MedicationPlanEntity
import com.example.diary.data.local.entity.symptom.SymptomEntity
import com.example.diary.data.local.entity.symptom.SymptomTypeEntity
import com.example.diary.data.local.entity.water.BeverageCategoryEntity
import com.example.diary.data.local.entity.water.WaterIntakeEntity
import com.example.diary.data.local.entity.workout.WorkoutSessionEntity
import com.example.diary.data.local.entity.workout.WorkoutTypeEntity
import com.example.diary.domain.model.user.*
import com.example.diary.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class ExportAllDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val database: AppDatabase
) {



    suspend operator fun invoke(): FullDataDump {
        val profile = userRepository.observeProfile().first()


        val meals            = database.mealDao().getAllMealsWithDetails().first()
        val ingredients      = database.ingredientDao().getAllIngredients()
        val cookingMethods   = database.cookingMethodDao().getAllCookingMethods()
        val mealIngredients  = database.mealIngredientDao().getAll()
        val symptoms         = database.symptomDao().getAll().first()
        val symptomTypes     = database.symptomTypeDao().getAll().first()
        val medicationDefs   = database.medicationDefinitionDao().getAll()
        val medicationPlans  = database.medicationPlanDao().getAll().first()
        val medicationLogs   = database.medicationLogDao().getAll()
        val waterIntakes     = database.waterIntakeDao().getAll()
        val beverageCategories = database.beverageCategoryDao().getAll().first()
        val sleepSessions    = database.sleepSessionDao().getAll()
        val stressLogs       = database.stressLogDao().getAll()
        val workoutSessions  = database.workoutSessionDao().getAll()
        val workoutTypes     = database.workoutTypeDao().getAll().first()

        return FullDataDump(
            profile = profile,
            meals = meals,
            ingredients = ingredients,
            cookingMethods = cookingMethods,
            mealIngredients = mealIngredients,
            symptoms = symptoms,
            symptomTypes = symptomTypes,
            medicationDefs = medicationDefs,
            medicationPlans = medicationPlans,
            medicationLogs = medicationLogs,
            waterIntakes = waterIntakes,
            beverageCategories = beverageCategories,
            sleepSessions = sleepSessions,
            stressLogs = stressLogs,
            workoutSessions = workoutSessions,
            workoutTypes = workoutTypes,
            files = emptyList()
        )
    }
}




data class FullDataDump(
    val profile: UserProfileModel?,
    val files: List<UserFileModel>,
    val meals: List<MealWithIngredientsAndMethod>,
    val ingredients: List<Ingredient>,
    val cookingMethods: List<CookingMethod>,
    val mealIngredients: List<MealIngredientCrossRef>,
    val symptoms: List<SymptomEntity>,
    val symptomTypes: List<SymptomTypeEntity>,
    val medicationDefs: List<MedicationDefinitionEntity>,
    val medicationPlans: List<MedicationPlanEntity>,
    val medicationLogs: List<MedicationLogEntity>,
    val waterIntakes: List<WaterIntakeEntity>,
    val beverageCategories: List<BeverageCategoryEntity>,
    val sleepSessions: List<SleepSessionEntity>,
    val stressLogs: List<StressLogEntity>,
    val workoutSessions: List<WorkoutSessionEntity>,
    val workoutTypes: List<WorkoutTypeEntity>
)
