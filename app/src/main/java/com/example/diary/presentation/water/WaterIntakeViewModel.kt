package com.example.diary.presentation.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.usecase.water.AddBeverageCategoryUseCase
import com.example.diary.domain.usecase.water.AddWaterIntakeUseCase
import com.example.diary.domain.usecase.water.DeleteBeverageCategoryUseCase
import com.example.diary.domain.usecase.water.DeleteWaterIntakeUseCase
import com.example.diary.domain.usecase.water.GetAllBeverageCategoriesUseCase
import com.example.diary.domain.usecase.water.GetWaterIntakesForDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WaterIntakeViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllBeverageCategoriesUseCase,
    private val addCategoryUseCase: AddBeverageCategoryUseCase,
    private val deleteCategoryUseCase: DeleteBeverageCategoryUseCase,
    private val getIntakesForDateUseCase: GetWaterIntakesForDateUseCase,
    private val addIntakeUseCase: AddWaterIntakeUseCase,
    private val deleteIntakeUseCase: DeleteWaterIntakeUseCase
) : ViewModel() {
    private val _categories = MutableStateFlow<List<BeverageCategory>>(emptyList())
    val categories: StateFlow<List<BeverageCategory>> = _categories

    private val _intakes = MutableStateFlow<List<WaterIntake>>(emptyList())
    val intakes: StateFlow<List<WaterIntake>> = _intakes

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    init {

        viewModelScope.launch {
            getAllCategoriesUseCase()
                .collect { list -> _categories.value = list }
        }

        loadIntakesForDate(_selectedDate.value)
    }


    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadIntakesForDate(date)
    }


    fun loadIntakesForDate(date: LocalDate) = viewModelScope.launch {
        getIntakesForDateUseCase(date)
            .collect { list -> _intakes.value = list }
    }


    fun addCategory(category: BeverageCategory) = viewModelScope.launch {
        addCategoryUseCase(category)

        _categories.value = getAllCategoriesUseCase().firstOrNull().orEmpty()
    }


    fun deleteCategory(category: BeverageCategory) = viewModelScope.launch {
        deleteCategoryUseCase(category)
        _categories.value = getAllCategoriesUseCase().firstOrNull().orEmpty()
    }


    fun addIntake(intake: WaterIntake) = viewModelScope.launch {
        addIntakeUseCase(intake)
        loadIntakesForDate(_selectedDate.value)
    }


    fun deleteIntake(intakeId: Long) = viewModelScope.launch {
        deleteIntakeUseCase(intakeId)
        loadIntakesForDate(_selectedDate.value)
    }
}
