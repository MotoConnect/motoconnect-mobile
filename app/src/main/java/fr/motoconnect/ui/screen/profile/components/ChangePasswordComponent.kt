package fr.motoconnect.ui.screen.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import fr.motoconnect.viewmodel.AuthenticationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordComponent(
    authenticationViewModel: AuthenticationViewModel
) {
    val password = remember { mutableStateOf("") }

    val confirmationPassword = remember { mutableStateOf("") }

    val isEditable = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            readOnly = !isEditable.value,
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = confirmationPassword.value,
            onValueChange = { confirmationPassword.value = it },
            label = { Text("Confirm Password") },
            readOnly = !isEditable.value,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = { isEditable.value = !isEditable.value }) {
            Text(
                text = if (isEditable.value) "Cancel" else "Edit",
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Button(onClick = {
            authenticationViewModel.changePassword(password.value, confirmationPassword.value)
        }) {
            Text(text = "Update Password", color = MaterialTheme.colorScheme.secondary)
        }
    }
}