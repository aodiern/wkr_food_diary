package com.example.diary.presentation.combined.profile

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.diary.presentation.navigation.NavRoutes
import java.security.MessageDigest
import java.util.Calendar
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val profile by viewModel.profileState.collectAsState()
    val context = LocalContext.current

    val exportUriFlow = remember { viewModel.exportUri }


    var isEditing       by remember { mutableStateOf(false) }
    var showSetPassword by remember { mutableStateOf(false) }
    var showDeletePass  by remember { mutableStateOf(false) }
    var showReminderDlg by remember { mutableStateOf(false) }

    var fio       by remember(profile) { mutableStateOf(profile?.fio.orEmpty()) }
    var dob       by remember(profile) { mutableStateOf(profile?.dob.orEmpty()) }
    var diagnoses by remember(profile) { mutableStateOf(profile?.diagnoses.orEmpty()) }

    var imageUri by remember(profile?.theme) {
        mutableStateOf(profile?.theme?.let { Uri.parse(it) })
    }



    var newPassword     by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError   by remember { mutableStateOf<String?>(null) }


    var reminderText   by remember { mutableStateOf("") }
    var reminderHour   by remember { mutableStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    var reminderMinute by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MINUTE)) }


    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            viewModel.saveProfile(
                fio = fio,
                dob = dob,
                diagnoses = diagnoses,
                theme = it.toString()
            )
        }
    }

    LaunchedEffect(exportUriFlow) {
        exportUriFlow.collect { uri ->
            context.grantUriPermission(
                context.packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(
                Intent.createChooser(shareIntent, "Экспорт данных")
            )
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = fio.ifBlank { "Фамилия Имя Отчество" },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Редактировать")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dob.ifBlank { "01.01.2000" },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = diagnoses.ifBlank { "Нет диагнозов" },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { imagePicker.launch("image/*") },
                shape = CircleShape,
                tonalElevation = 4.dp
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Фото")
                    }
                }
            }
        }


        if (isEditing) {
            AlertDialog(
                onDismissRequest = { isEditing = false },
                title   = { Text("Редактировать профиль") },
                text    = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = fio,
                            onValueChange = { fio = it },
                            label = { Text("ФИО") }
                        )
                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            label = { Text("Дата рождения") }
                        )
                        OutlinedTextField(
                            value = diagnoses,
                            onValueChange = { diagnoses = it },
                            label = { Text("Диагнозы") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.saveProfile(fio, dob, diagnoses)
                        isEditing = false
                    }) { Text("Сохранить") }
                },
                dismissButton = {
                    TextButton(onClick = { isEditing = false }) { Text("Отмена") }
                }
            )
        }


        if (showSetPassword) {
            AlertDialog(
                onDismissRequest = {
                    showSetPassword = false
                    passwordError = null
                },
                title   = { Text("Установить пароль") },
                text    = {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Новый пароль") },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = passwordError != null
                        )
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Повтор пароля") },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = passwordError != null
                        )
                        passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newPassword.isBlank() || newPassword != confirmPassword) {
                            passwordError = "Пароли не совпадают"
                        } else {
                            val hash = MessageDigest
                                .getInstance("SHA-256")
                                .digest(newPassword.toByteArray())
                                .joinToString("") { "%02x".format(it) }
                            viewModel.saveProfile(fio, dob, diagnoses, passwordHash = hash)
                            showSetPassword = false
                        }
                    }) {
                        Text("ОК")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showSetPassword = false
                        passwordError = null
                    }) {
                        Text("Отмена")
                    }
                }
            )
        }


        if (showDeletePass) {
            AlertDialog(
                onDismissRequest = { showDeletePass = false },
                title   = { Text("Удалить пароль") },
                text    = { Text("Вы уверены, что хотите удалить пароль?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.removePassword()
                        showDeletePass = false
                    }) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeletePass = false }) {
                        Text("Отмена")
                    }
                }
            )
        }


        if (showReminderDlg) {
            AlertDialog(
                onDismissRequest = { showReminderDlg = false },
                title   = { Text("Новое напоминание") },
                text    = {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = reminderText,
                            onValueChange = { reminderText = it },
                            label = { Text("Текст напоминания") },
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, h, m ->
                                        reminderHour = h
                                        reminderMinute = m
                                    },
                                    reminderHour,
                                    reminderMinute,
                                    true
                                ).show()
                            }
                        ) {
                            Text("Выбрать время: %02d:%02d".format(reminderHour, reminderMinute))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {

                        val cal = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, reminderHour)
                            set(Calendar.MINUTE, reminderMinute)
                            set(Calendar.SECOND, 0)
                        }
                        viewModel.scheduleReminder(context, cal.timeInMillis, reminderText)
                        showReminderDlg = false
                    }) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReminderDlg = false }) {
                        Text("Отмена")
                    }
                }
            )
        }


        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { showSetPassword = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Поставить пароль") }

            Button(
                onClick = { showDeletePass = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Удалить пароль") }

            Button(
                onClick = { viewModel.exportData(context) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Экспорт данных") }


            Button(
                onClick = { navController.navigate(NavRoutes.Notifications.route) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Настроить уведомления") }

        }

    }
}
