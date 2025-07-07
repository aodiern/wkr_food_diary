package com.example.diary.di

import android.content.Context
import androidx.room.Room
import com.example.diary.data.local.dao.SleepSessionDao
import com.example.diary.data.local.dao.StressLogDao
import com.example.diary.data.local.db.AppDatabase
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
import com.example.diary.data.repository.SleepSessionRepositoryImpl
import com.example.diary.data.repository.StatsRepository
import com.example.diary.data.repository.StressLogRepositoryImpl
import com.example.diary.data.repository.UserRepositoryImpl
import com.example.diary.data.repository.meal.CookingMethodRepositoryImpl
import com.example.diary.data.repository.meal.IngredientRepositoryImpl
import com.example.diary.data.repository.meal.MealIngredientRepositoryImpl
import com.example.diary.data.repository.meal.MealRepositoryImpl
import com.example.diary.data.repository.symptom.SymptomRepositoryImpl
import com.example.diary.data.repository.symptom.SymptomTypeRepositoryImpl
import com.example.diary.data.repository.medication.MedicationDefinitionRepositoryImpl
import com.example.diary.data.repository.medication.MedicationPlanRepositoryImpl
import com.example.diary.data.repository.medication.MedicationLogRepositoryImpl
import com.example.diary.data.repository.water.BeverageCategoryRepositoryImpl
import com.example.diary.data.repository.water.WaterIntakeRepositoryImpl
import com.example.diary.data.repository.workout.WorkoutSessionRepositoryImpl
import com.example.diary.data.repository.workout.WorkoutTypeRepositoryImpl
import com.example.diary.domain.repository.SleepSessionRepository
import com.example.diary.domain.repository.StressLogRepository
import com.example.diary.domain.repository.UserRepository

import com.example.diary.domain.repository.meal.CookingMethodRepository
import com.example.diary.domain.repository.meal.IngredientRepository
import com.example.diary.domain.repository.meal.MealIngredientRepository
import com.example.diary.domain.repository.meal.MealRepository
import com.example.diary.domain.repository.symptom.SymptomRepository
import com.example.diary.domain.repository.symptom.SymptomTypeRepository
import com.example.diary.domain.repository.medication.MedicationDefinitionRepository
import com.example.diary.domain.repository.medication.MedicationLogRepository
import com.example.diary.domain.repository.medication.MedicationPlanRepository
import com.example.diary.domain.repository.water.BeverageCategoryRepository
import com.example.diary.domain.repository.water.WaterIntakeRepository
import com.example.diary.domain.repository.workout.WorkoutSessionRepository
import com.example.diary.domain.repository.workout.WorkoutTypeRepository
import com.example.diary.domain.usecase.AddWorkoutSessionUseCase
import com.example.diary.domain.usecase.*
import com.example.diary.domain.usecase.DeleteWorkoutSessionUseCase
import com.example.diary.domain.usecase.DeleteWorkoutTypeUseCase
import com.example.diary.domain.usecase.GetAllWorkoutTypesUseCase
import com.example.diary.domain.usecase.GetWorkoutSessionsForDateUseCase

import com.example.diary.domain.usecase.meal.*
import com.example.diary.domain.usecase.symptom.*
import com.example.diary.domain.usecase.medication.medicationDefinition.AddMedicationDefinitionUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.DeleteMedicationDefinitionUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.GetAllMedicationDefinitionsUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.GetMedicationDefinitionUseCase
import com.example.diary.domain.usecase.medication.medicationDefinition.UpdateMedicationDefinitionUseCase
import com.example.diary.domain.usecase.medication.medicationLog.AddMedicationLogUseCase
import com.example.diary.domain.usecase.medication.medicationLog.DeleteMedicationLogUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GenerateMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GetMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.medication.medicationLog.UpdateMedicationLogUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.AddMedicationPlanUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.DeleteMedicationPlanUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.GetAllMedicationPlansUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.GetMedicationPlanUseCase
import com.example.diary.domain.usecase.medication.medicationPlan.UpdateMedicationPlanUseCase
import com.example.diary.domain.usecase.water.AddBeverageCategoryUseCase
import com.example.diary.domain.usecase.water.AddWaterIntakeUseCase
import com.example.diary.domain.usecase.water.DeleteBeverageCategoryUseCase
import com.example.diary.domain.usecase.water.DeleteWaterIntakeUseCase
import com.example.diary.domain.usecase.water.GetAllBeverageCategoriesUseCase
import com.example.diary.domain.usecase.water.GetWaterIntakesForDateUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "meal_db"
    )
        .fallbackToDestructiveMigration()
        .build()

    
    @Provides fun provideMealDao(db: AppDatabase): MealDao = db.mealDao()
    @Provides fun provideIngredientDao(db: AppDatabase): IngredientDao = db.ingredientDao()
    @Provides fun provideMethodDao(db: AppDatabase): CookingMethodDao = db.cookingMethodDao()
    @Provides fun provideCrossRefDao(db: AppDatabase): MealIngredientDao = db.mealIngredientDao()
    @Provides fun provideSymptomDao(db: AppDatabase): SymptomDao = db.symptomDao()
    @Provides fun provideSymptomTypeDao(db: AppDatabase): SymptomTypeDao = db.symptomTypeDao()
    @Provides fun provideMedicationDefinitionDao(db: AppDatabase): MedicationDefinitionDao = db.medicationDefinitionDao()
    @Provides fun provideMedicationPlanDao(db: AppDatabase): MedicationPlanDao = db.medicationPlanDao()
    @Provides fun provideMedicationLogDao(db: AppDatabase): MedicationLogDao = db.medicationLogDao()
    @Provides
    fun provideBeverageCategoryDao(db: AppDatabase): BeverageCategoryDao = db.beverageCategoryDao()
    @Provides
    @Singleton
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()
    @Provides
    fun provideWaterIntakeDao(db: AppDatabase): WaterIntakeDao = db.waterIntakeDao()

    @Provides
    fun provideSleepSessionDao(db: AppDatabase): SleepSessionDao =
        db.sleepSessionDao()

    @Provides
    @Singleton
    fun provideWorkoutTypeDao(db: AppDatabase): WorkoutTypeDao = db.workoutTypeDao()

    @Provides
    @Singleton
    fun provideWorkoutSessionDao(db: AppDatabase): WorkoutSessionDao = db.workoutSessionDao()
    @Provides
    @Singleton
    fun provideStressLogDao(db: AppDatabase): StressLogDao =
        db.stressLogDao()

    @Provides
    @Singleton
    fun provideStressLogRepository(
        dao: StressLogDao
    ): StressLogRepository =
        StressLogRepositoryImpl(dao)

    
    @Provides
    @Singleton
    fun provideWorkoutTypeRepository(
        dao: WorkoutTypeDao
    ): WorkoutTypeRepository = WorkoutTypeRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserRepository(
        profileDao: ProfileDao
    ): UserRepository = UserRepositoryImpl(profileDao)

    @Provides
    @Singleton
    fun provideWorkoutSessionRepository(
        dao: WorkoutSessionDao
    ): WorkoutSessionRepository = WorkoutSessionRepositoryImpl(dao)

    
    @Provides
    fun provideSleepSessionRepository(
        dao: SleepSessionDao
    ): SleepSessionRepository =
        SleepSessionRepositoryImpl(dao)
    
    @Provides fun provideMealRepository(
        dao: MealDao
    ): MealRepository = MealRepositoryImpl(dao)

    @Provides fun provideIngredientRepository(
        dao: IngredientDao
    ): IngredientRepository = IngredientRepositoryImpl(dao)

    @Provides fun provideCookingMethodRepository(
        dao: CookingMethodDao
    ): CookingMethodRepository = CookingMethodRepositoryImpl(dao)

    @Provides fun provideMealIngredientRepository(
        dao: MealIngredientDao
    ): MealIngredientRepository = MealIngredientRepositoryImpl(dao)

    @Provides fun provideSymptomRepository(
        dao: SymptomDao
    ): SymptomRepository = SymptomRepositoryImpl(dao)

    @Provides fun provideSymptomTypeRepository(
        dao: SymptomTypeDao
    ): SymptomTypeRepository = SymptomTypeRepositoryImpl(dao)

    
    @Provides fun provideMedicationDefinitionRepository(
        dao: MedicationDefinitionDao
    ): MedicationDefinitionRepository = MedicationDefinitionRepositoryImpl(dao)

    @Provides fun provideMedicationPlanRepository(
        dao: MedicationPlanDao
    ): MedicationPlanRepository = MedicationPlanRepositoryImpl(dao)

    @Provides
    fun provideMedicationLogRepository(
        logDao: MedicationLogDao,
        planDao: MedicationPlanDao,
        definitionDao: MedicationDefinitionDao
    ): MedicationLogRepository =
        MedicationLogRepositoryImpl(logDao, planDao, definitionDao)

    @Provides
    fun provideBeverageCategoryRepository(
        dao: BeverageCategoryDao
    ): BeverageCategoryRepository = BeverageCategoryRepositoryImpl(dao)


    @Provides
    fun provideWaterIntakeRepository(
        dao: WaterIntakeDao
    ): WaterIntakeRepository = WaterIntakeRepositoryImpl(dao)

    
    @Provides fun provideGetAllMealsWithDetailsUseCase(
        repo: MealRepository
    ): GetAllMealsWithDetailsUseCase = GetAllMealsWithDetailsUseCase(repo)
    @Provides fun provideAddMealUseCase(
        repo: MealRepository
    ): AddMealUseCase = AddMealUseCase(repo)
    @Provides fun provideAddMealIngredientUseCase(
        repo: MealIngredientRepository
    ): AddMealIngredientUseCase = AddMealIngredientUseCase(repo)
    @Provides fun provideGetAllIngredientsUseCase(
        repo: IngredientRepository
    ): GetAllIngredientsUseCase = GetAllIngredientsUseCase(repo)
    @Provides fun provideGetAllCookingMethodsUseCase(
        repo: CookingMethodRepository
    ): GetAllCookingMethodsUseCase = GetAllCookingMethodsUseCase(repo)
    @Provides fun provideUpdateIngredientUseCase(
        repo: IngredientRepository
    ): UpdateIngredientUseCase = UpdateIngredientUseCase(repo)
    @Provides fun provideDeleteIngredientUseCase(
        repo: IngredientRepository
    ): DeleteIngredientUseCase = DeleteIngredientUseCase(repo)
    @Provides fun provideUpdateMealUseCase(
        repo: MealRepository
    ): UpdateMealUseCase = UpdateMealUseCase(repo)
    @Provides fun provideDeleteMealUseCase(
        repo: MealRepository
    ): DeleteMealUseCase = DeleteMealUseCase(repo)

    @Provides
    fun provideGetWaterIntakesForDateUseCase(
        repo: WaterIntakeRepository
    ): GetWaterIntakesForDateUseCase = GetWaterIntakesForDateUseCase(repo)

    @Provides
    fun provideAddWaterIntakeUseCase(
        repo: WaterIntakeRepository
    ): AddWaterIntakeUseCase = AddWaterIntakeUseCase(repo)
    
    @Provides fun provideGetAllSymptomsUseCase(
        repo: SymptomRepository
    ): GetAllSymptomsUseCase = GetAllSymptomsUseCase(repo)
    @Provides fun provideAddSymptomUseCase(
        repo: SymptomRepository
    ): AddSymptomUseCase = AddSymptomUseCase(repo)
    @Provides fun provideUpdateSymptomUseCase(
        repo: SymptomRepository
    ): UpdateSymptomUseCase = UpdateSymptomUseCase(repo)
    @Provides fun provideDeleteSymptomUseCase(
        repo: SymptomRepository
    ): DeleteSymptomUseCase = DeleteSymptomUseCase(repo)
    @Provides fun provideGetAllSymptomTypesUseCase(
        repo: SymptomTypeRepository
    ): GetAllSymptomTypesUseCase = GetAllSymptomTypesUseCase(repo)
    @Provides fun provideAddSymptomTypeUseCase(
        repo: SymptomTypeRepository
    ): AddSymptomTypeUseCase = AddSymptomTypeUseCase(repo)
    @Provides fun provideUpdateSymptomTypeUseCase(
        repo: SymptomTypeRepository
    ): UpdateSymptomTypeUseCase = UpdateSymptomTypeUseCase(repo)
    @Provides fun provideDeleteSymptomTypeUseCase(
        repo: SymptomTypeRepository
    ): DeleteSymptomTypeUseCase = DeleteSymptomTypeUseCase(repo)

    
    @Provides fun provideGetAllMedicationDefinitionsUseCase(
        repo: MedicationDefinitionRepository
    ): GetAllMedicationDefinitionsUseCase = GetAllMedicationDefinitionsUseCase(repo)
    @Provides fun provideGetMedicationDefinitionUseCase(
        repo: MedicationDefinitionRepository
    ): GetMedicationDefinitionUseCase = GetMedicationDefinitionUseCase(repo)
    @Provides fun provideAddMedicationDefinitionUseCase(
        repo: MedicationDefinitionRepository
    ): AddMedicationDefinitionUseCase = AddMedicationDefinitionUseCase(repo)
    @Provides fun provideUpdateMedicationDefinitionUseCase(
        repo: MedicationDefinitionRepository
    ): UpdateMedicationDefinitionUseCase = UpdateMedicationDefinitionUseCase(repo)
    @Provides fun provideDeleteMedicationDefinitionUseCase(
        repo: MedicationDefinitionRepository
    ): DeleteMedicationDefinitionUseCase = DeleteMedicationDefinitionUseCase(repo)

    
    @Provides fun provideGetAllMedicationPlansUseCase(
        repo: MedicationPlanRepository
    ): GetAllMedicationPlansUseCase = GetAllMedicationPlansUseCase(repo)
    @Provides fun provideGetMedicationPlanUseCase(
        repo: MedicationPlanRepository
    ): GetMedicationPlanUseCase = GetMedicationPlanUseCase(repo)
    @Provides fun provideAddMedicationPlanUseCase(
        repo: MedicationPlanRepository
    ): AddMedicationPlanUseCase = AddMedicationPlanUseCase(repo)
    @Provides fun provideUpdateMedicationPlanUseCase(
        repo: MedicationPlanRepository
    ): UpdateMedicationPlanUseCase = UpdateMedicationPlanUseCase(repo)
    @Provides fun provideDeleteMedicationPlanUseCase(
        repo: MedicationPlanRepository
    ): DeleteMedicationPlanUseCase = DeleteMedicationPlanUseCase(repo)

    
    @Provides fun provideGetMedicationLogsForDateUseCase(
        repo: MedicationLogRepository
    ): GetMedicationLogsForDateUseCase = GetMedicationLogsForDateUseCase(repo)
    @Provides fun provideAddMedicationLogUseCase(
        repo: MedicationLogRepository
    ): AddMedicationLogUseCase = AddMedicationLogUseCase(repo)
    @Provides fun provideUpdateMedicationLogUseCase(
        repo: MedicationLogRepository
    ): UpdateMedicationLogUseCase = UpdateMedicationLogUseCase(repo)
    @Provides fun provideDeleteMedicationLogUseCase(
        repo: MedicationLogRepository
    ): DeleteMedicationLogUseCase = DeleteMedicationLogUseCase(repo)
    @Provides fun provideGenerateMedicationLogsForDateUseCase(
        planRepo: MedicationPlanRepository,
        logRepo: MedicationLogRepository
    ): GenerateMedicationLogsForDateUseCase = GenerateMedicationLogsForDateUseCase(planRepo, logRepo)
    @Provides
    fun provideDeleteWaterIntakeUseCase(
        repo: WaterIntakeRepository
    ): DeleteWaterIntakeUseCase = DeleteWaterIntakeUseCase(repo)

    
    @Provides
    fun provideGetAllBeverageCategoriesUseCase(
        repo: BeverageCategoryRepository
    ): GetAllBeverageCategoriesUseCase = GetAllBeverageCategoriesUseCase(repo)

    @Provides
    fun provideAddBeverageCategoryUseCase(
        repo: BeverageCategoryRepository
    ): AddBeverageCategoryUseCase = AddBeverageCategoryUseCase(repo)

    @Provides
    fun provideDeleteBeverageCategoryUseCase(
        repo: BeverageCategoryRepository
    ): DeleteBeverageCategoryUseCase = DeleteBeverageCategoryUseCase(repo)

    @Provides
    fun provideGetSleepSessionsForDateUseCase(
        repo: com.example.diary.domain.repository.SleepSessionRepository
    ): com.example.diary.domain.usecase.sleep.GetSleepSessionsForDateUseCase =
        com.example.diary.domain.usecase.sleep.GetSleepSessionsForDateUseCase(repo)

    @Provides
    fun provideAddSleepSessionUseCase(
        repo: com.example.diary.domain.repository.SleepSessionRepository
    ): com.example.diary.domain.usecase.sleep.AddSleepSessionUseCase =
        com.example.diary.domain.usecase.sleep.AddSleepSessionUseCase(repo)

    @Provides
    fun provideDeleteSleepSessionUseCase(
        repo: com.example.diary.domain.repository.SleepSessionRepository
    ): com.example.diary.domain.usecase.sleep.DeleteSleepSessionUseCase =
        com.example.diary.domain.usecase.sleep.DeleteSleepSessionUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAllWorkoutTypesUseCase(
        repo: WorkoutTypeRepository
    ): GetAllWorkoutTypesUseCase = GetAllWorkoutTypesUseCase(repo)

    @Provides
    @Singleton
    fun provideAddWorkoutTypeUseCase(
        repo: WorkoutTypeRepository
    ): AddWorkoutTypeUseCase = AddWorkoutTypeUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteWorkoutTypeUseCase(
        repo: WorkoutTypeRepository
    ): DeleteWorkoutTypeUseCase = DeleteWorkoutTypeUseCase(repo)

    
    @Provides
    @Singleton
    fun provideGetWorkoutSessionsForDateUseCase(
        repo: WorkoutSessionRepository
    ): GetWorkoutSessionsForDateUseCase = GetWorkoutSessionsForDateUseCase(repo)

    @Provides
    @Singleton
    fun provideAddWorkoutSessionUseCase(
        repo: WorkoutSessionRepository
    ): AddWorkoutSessionUseCase = AddWorkoutSessionUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteWorkoutSessionUseCase(
        repo: WorkoutSessionRepository
    ): DeleteWorkoutSessionUseCase = DeleteWorkoutSessionUseCase(repo)

    @Provides
    fun provideObserveProfileUseCase(
        repository: UserRepository
    ): ObserveProfileUseCase = ObserveProfileUseCase(repository)

    @Provides
    fun provideSaveProfileUseCase(
        repository: UserRepository
    ): SaveProfileUseCase = SaveProfileUseCase(repository)



    @Provides
    fun provideExportAllDataUseCase(
        repository: UserRepository,
        database: AppDatabase
    ): ExportAllDataUseCase = ExportAllDataUseCase(repository, database)



    @Provides
    @Singleton
    fun provideStatsRepository(
        sleepDao: SleepSessionDao,
        waterDao: WaterIntakeDao,
        stressDao: StressLogDao,
        symptomDao: SymptomDao
    ): StatsRepository = StatsRepository(
        sleepDao     = sleepDao,
        waterDao     = waterDao,
        stressDao    = stressDao,
        symptomDao   = symptomDao
    )
}

