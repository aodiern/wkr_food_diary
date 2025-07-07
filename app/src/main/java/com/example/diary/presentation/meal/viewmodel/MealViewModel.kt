package com.example.diary.presentation.meal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.data.mapper.toUi
import com.example.diary.domain.repository.meal.CookingMethodRepository
import com.example.diary.domain.repository.meal.IngredientRepository
import com.example.diary.domain.usecase.meal.AddMealIngredientUseCase
import com.example.diary.domain.usecase.meal.AddMealUseCase
import com.example.diary.domain.usecase.meal.DeleteIngredientUseCase
import com.example.diary.domain.usecase.meal.DeleteMealIngredientUseCase
import com.example.diary.domain.usecase.meal.DeleteMealUseCase
import com.example.diary.domain.usecase.meal.GetAllCookingMethodsUseCase
import com.example.diary.domain.usecase.meal.GetAllIngredientsUseCase
import com.example.diary.domain.usecase.meal.GetAllMealsWithDetailsUseCase
import com.example.diary.domain.usecase.meal.UpdateIngredientUseCase
import com.example.diary.domain.usecase.meal.UpdateMealUseCase
import com.example.diary.presentation.meal.model.MealUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val getAllMealsWithDetailsUseCase: GetAllMealsWithDetailsUseCase,
    private val getAllCookingMethodsUseCase: GetAllCookingMethodsUseCase,
    private val getAllIngredientsUseCase: GetAllIngredientsUseCase,
    private val cookingMethodRepository: CookingMethodRepository,
    private val ingredientRepository: IngredientRepository,
    private val addMealUseCase: AddMealUseCase,
    private val addMealIngredientUseCase: AddMealIngredientUseCase,
    private val deleteMealIngredientUseCase: DeleteMealIngredientUseCase,
    private val updateMealUseCase: UpdateMealUseCase,
    private val deleteMealUseCase: DeleteMealUseCase,
    private val updateIngredientUseCase: UpdateIngredientUseCase,
    private val deleteIngredientUseCase: DeleteIngredientUseCase
) : ViewModel() {

    private val _meals = MutableStateFlow<List<MealUiModel>>(emptyList())
    val meals: StateFlow<List<MealUiModel>> get() = _meals

    private val _methods = MutableStateFlow<List<CookingMethod>>(emptyList())
    val methods: StateFlow<List<CookingMethod>> get() = _methods

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> get() = _ingredients


    init {

        viewModelScope.launch {
            getAllMealsWithDetailsUseCase()
                .map { domainList -> domainList.map { it.toUi() } }
                .collect { uiList ->
                    _meals.value = uiList
                }
        }


        viewModelScope.launch {
            val current = getAllCookingMethodsUseCase()
            if (current.isEmpty()) {
                val defaults = listOf(
                    CookingMethod(methodName = "Без способа приготовления"),
                    CookingMethod(methodName = "Жарка"),
                    CookingMethod(methodName = "Варка"),
                    CookingMethod(methodName = "На пару"),
                    CookingMethod(methodName = "Запекание"),
                    CookingMethod(methodName = "Тушение"),
                    CookingMethod(methodName = "Гриль"),
                    CookingMethod(methodName = "Копчение"),
                    CookingMethod(methodName = "Соление"),
                    CookingMethod(methodName = "Консервирование"),
                    CookingMethod(methodName = "Заморозка")
                )
                cookingMethodRepository.insertAll(defaults)
                _methods.value = cookingMethodRepository.getAllMethods()
            } else {
                _methods.value = current
            }
        }


        viewModelScope.launch {
            _ingredients.value = getAllIngredientsUseCase()
        }
    }


    fun addMealWithBoth(
        name: String,
        methodId: Long,
        newIngredientNames: List<String>,
        existingIngredientIds: List<Long>,
        dateTime: LocalDateTime = LocalDateTime.now()
    ) = viewModelScope.launch {

        val mealId = addMealUseCase(Meal(0, name, methodId, dateTime))


        val allIds = mutableListOf<Long>()
        for (ingName in newIngredientNames) {
            val dbList = getAllIngredientsUseCase()
            val existing = dbList.firstOrNull { it.name.equals(ingName, true) }
            val ingId = existing?.ingredientId
                ?: ingredientRepository.insertIngredient(Ingredient(name = ingName))
            allIds += ingId
        }


        allIds += existingIngredientIds


        allIds.forEach { addMealIngredientUseCase(mealId, it) }
    }







    fun editMeal(
        mealId: Long,
        newName: String,
        newMethodId: Long,
        newIngredientIds: List<Long>,
        newDateTime: LocalDateTime
    ) = viewModelScope.launch {
        require(newName.isNotBlank()) { "Название не может быть пустым" }
        updateMealUseCase(Meal(mealId, newName, newMethodId, newDateTime))


        val allMeals = getAllMealsWithDetailsUseCase().firstOrNull() ?: emptyList()
        val currentIds = allMeals
            .firstOrNull { it.meal.mealId == mealId }
            ?.ingredients
            ?.map { it.ingredientId }
            .orEmpty()


        currentIds
            .filterNot { it in newIngredientIds }
            .forEach { deleteMealIngredientUseCase(mealId, it) }


        newIngredientIds
            .filterNot { it in currentIds }
            .forEach { addMealIngredientUseCase(mealId, it) }
    }


    fun removeMeal(mealId: Long) = viewModelScope.launch {
        val mealsWithDetails = getAllMealsWithDetailsUseCase()
            .firstOrNull()
            ?: return@launch


        val existingMealEntity = mealsWithDetails
            .firstOrNull { it.meal.mealId == mealId }
            ?.meal
            ?: return@launch


        deleteMealUseCase(existingMealEntity)
    }


    fun editIngredient(ingredientId: Long, newName: String) = viewModelScope.launch {
        require(newName.isNotBlank()) { "Имя не может быть пустым" }
        val existing = getAllIngredientsUseCase().firstOrNull { it.ingredientId == ingredientId }
        existing?.let {
            updateIngredientUseCase(it.copy(name = newName))
            _ingredients.value = getAllIngredientsUseCase()
        }
    }


    fun removeIngredient(ingredientId: Long) = viewModelScope.launch {
        val existing = getAllIngredientsUseCase().firstOrNull { it.ingredientId == ingredientId }
        existing?.let {
            deleteIngredientUseCase(it)
            _ingredients.value = getAllIngredientsUseCase()
        }
    }
}
