package com.example.diary.presentation.combined.profile

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.user.UserProfileModel
import com.example.diary.domain.usecase.ExportAllDataUseCase
import com.example.diary.domain.usecase.ObserveProfileUseCase
import com.example.diary.domain.usecase.SaveProfileUseCase
import com.example.diary.notification.ReminderScheduler
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import java.util.Calendar




@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val exportAllDataUseCase: ExportAllDataUseCase
) : ViewModel() {

    private val _exportUri = MutableSharedFlow<Uri>()
    val exportUri: SharedFlow<Uri> = _exportUri

    private val _shareEvent = MutableSharedFlow<Uri>()
    val shareEvent = _shareEvent.asSharedFlow()
    val profileState: StateFlow<UserProfileModel?> = observeProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)


    init {

        viewModelScope.launch {
            val existing = observeProfileUseCase().firstOrNull()
            if (existing == null) {
                val defaultProfile = UserProfileModel(
                    fio = "Фамилия Имя Отчество",
                    dob = "01.01.2000",
                    diagnoses = "",
                    passwordHash = null,
                    theme = null
                )
                saveProfileUseCase(defaultProfile)
            }
        }
    }
    fun scheduleReminder(context: Context, timeInMillis: Long, message: String) {

        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
        }
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)


        ReminderScheduler.scheduleDailyReminder(
            context = context,
            hour = hour,
            minute = minute,
            message = message,
            requestCode = 123
        )
    }


    fun exportData(context: Context) {
        viewModelScope.launch {
            val dump = exportAllDataUseCase()
            val json = Gson().toJson(dump)
            val file = File(context.cacheDir, "app_dump_${System.currentTimeMillis()}.json")
            file.writeText(json)
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", file
            )
            _exportUri.emit(uri)
        }
    }



    fun saveProfile(
        fio: String,
        dob: String,
        diagnoses: String,
        passwordHash: String? = null,
        theme: String? = null
    ) {
        viewModelScope.launch {
            val profile = UserProfileModel(
                fio = fio,
                dob = dob,
                diagnoses = diagnoses,
                passwordHash = passwordHash,
                theme = theme
            )
            saveProfileUseCase(profile)
        }
    }

    fun removePassword() {
        viewModelScope.launch {
            profileState.value?.let { current ->
                val updated = current.copy(passwordHash = null)
                saveProfileUseCase(updated)
            }
        }
    }

}

