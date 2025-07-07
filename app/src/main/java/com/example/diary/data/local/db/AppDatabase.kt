package com.example.diary.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.diary.data.local.converter.Converters
import com.example.diary.data.local.dao.SleepSessionDao
import com.example.diary.data.local.dao.StressLogDao
import com.example.diary.data.local.dao.meal.CookingMethodDao
import com.example.diary.data.local.dao.meal.IngredientDao
import com.example.diary.data.local.dao.meal.MealDao
import com.example.diary.data.local.dao.meal.MealIngredientDao
import com.example.diary.data.local.dao.medication.MedicationDefinitionDao
import com.example.diary.data.local.dao.medication.MedicationLogDao
import com.example.diary.data.local.dao.medication.MedicationPlanDao
import com.example.diary.data.local.dao.symptom.SymptomDao
import com.example.diary.data.local.dao.symptom.SymptomTypeDao
import com.example.diary.data.local.dao.user.ProfileDao
import com.example.diary.data.local.dao.water.BeverageCategoryDao
import com.example.diary.data.local.dao.water.WaterIntakeDao
import com.example.diary.data.local.dao.workout.WorkoutSessionDao
import com.example.diary.data.local.dao.workout.WorkoutTypeDao
import com.example.diary.data.local.entity.SleepSessionEntity
import com.example.diary.data.local.entity.StressLogEntity
import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.data.local.entity.meal.MealIngredientCrossRef
import com.example.diary.data.local.entity.symptom.SymptomEntity
import com.example.diary.data.local.entity.symptom.SymptomTypeEntity
// Новые сущности для лекарств
import com.example.diary.data.local.entity.medication.MedicationDefinitionEntity
import com.example.diary.data.local.entity.medication.MedicationPlanEntity
import com.example.diary.data.local.entity.medication.MedicationLogEntity
import com.example.diary.data.local.entity.user.UserProfileEntity
import com.example.diary.data.local.entity.water.BeverageCategoryEntity
import com.example.diary.data.local.entity.water.WaterIntakeEntity
import com.example.diary.data.local.entity.workout.WorkoutSessionEntity
import com.example.diary.data.local.entity.workout.WorkoutTypeEntity

@Database(
    entities = [
        Meal::class,
        Ingredient::class,
        CookingMethod::class,
        MealIngredientCrossRef::class,
        SymptomEntity::class,
        SymptomTypeEntity::class,
        MedicationDefinitionEntity::class,
        MedicationPlanEntity::class,
        MedicationLogEntity::class,
        BeverageCategoryEntity::class,
        WaterIntakeEntity::class,
        SleepSessionEntity::class,
        StressLogEntity::class,
        WorkoutSessionEntity::class,
        WorkoutTypeEntity::class,
        UserProfileEntity::class
    ],
    version = 11
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun cookingMethodDao(): CookingMethodDao
    abstract fun mealIngredientDao(): MealIngredientDao
    abstract fun symptomDao(): SymptomDao
    abstract fun symptomTypeDao(): SymptomTypeDao
    abstract fun medicationDefinitionDao(): MedicationDefinitionDao
    abstract fun medicationPlanDao(): MedicationPlanDao
    abstract fun medicationLogDao(): MedicationLogDao
    abstract fun beverageCategoryDao(): BeverageCategoryDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun sleepSessionDao(): SleepSessionDao
    abstract fun stressLogDao(): StressLogDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutTypeDao(): WorkoutTypeDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun build(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            listOf(
                                "Без способа приготовления",
                                "Жарка",
                                "Варка",
                                "Запекание",
                                "Тушение",
                                "На пару",
                                "Гриль",
                                "Микроволновка",
                                "Консервирование",
                                "Копчение",
                                "Соление"
                            ).forEach { method ->
                                db.execSQL("INSERT INTO cooking_methods (methodName) VALUES ('$method')")
                            }
                        }

                    })
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }


            }
    }
}
