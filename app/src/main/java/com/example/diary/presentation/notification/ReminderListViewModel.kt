package com.example.diary.presentation.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.notification.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar as JCalendar

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _reminders = MutableStateFlow<List<ScheduledReminder>>(emptyList())
    val reminders: StateFlow<List<ScheduledReminder>> = _reminders

    private var nextId = 1


    fun addReminder(reminder: ScheduledReminder) {
        viewModelScope.launch {
            val id = nextId++
            val remWithId = reminder.copy(id = id)
            schedule(remWithId)
            _reminders.value = _reminders.value + remWithId
        }
    }


    fun updateReminder(reminder: ScheduledReminder) {
        viewModelScope.launch {

            ReminderScheduler.cancelReminder(appContext, reminder.id)

            schedule(reminder)
            _reminders.value = _reminders.value.map {
                if (it.id == reminder.id) reminder else it
            }
        }
    }


    fun deleteReminder(reminder: ScheduledReminder) {
        viewModelScope.launch {
            ReminderScheduler.cancelReminder(appContext, reminder.id)
            _reminders.value = _reminders.value.filterNot { it.id == reminder.id }
        }
    }


    private fun schedule(rem: ScheduledReminder) {
        val cal = JCalendar.getInstance().apply {
            timeInMillis = rem.timeInMillis
        }
        val hour   = cal.get(JCalendar.HOUR_OF_DAY)
        val minute = cal.get(JCalendar.MINUTE)

        ReminderScheduler.scheduleDailyReminder(
            context     = appContext,
            hour        = hour,
            minute      = minute,
            message     = rem.message,
            requestCode = rem.id
        )
    }
}
