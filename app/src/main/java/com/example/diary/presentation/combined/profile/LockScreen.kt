package com.example.diary.presentation.combined.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.security.MessageDigest





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onUnlock: () -> Unit
) {

    val profile by viewModel.profileState.collectAsState()
    val storedHash = profile?.passwordHash

    var inputPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }


    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(text = "Введите пароль", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = inputPassword,
                onValueChange = {
                    inputPassword = it
                    errorText = null
                },
                label = { Text("Пароль") },
                placeholder = { Text("••••••") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = errorText != null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { /* handled by button */ }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            errorText?.let { Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top=8.dp)) }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if (storedHash.isNullOrEmpty()) {
                    errorText = "Пароль не установлен"
                } else {

                    val hash = MessageDigest.getInstance("SHA-256")
                        .digest(inputPassword.toByteArray())
                        .joinToString("") { "%02x".format(it) }
                    if (hash == storedHash) {
                        onUnlock()
                    } else {
                        errorText = "Неверный пароль"
                    }
                }
            }) {
                Text("Войти")
            }
        }
    }
}
