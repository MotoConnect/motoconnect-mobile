package fr.motoconnect.ui.screen.profile.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.motoconnect.viewmodel.AuthenticationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeUsernameComponent(
    authenticationViewModel: AuthenticationViewModel
) {
    val username = remember { mutableStateOf("") }

    val isEditable = remember { mutableStateOf(false) }

    val authUiState by authenticationViewModel.authUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") },
            readOnly = !isEditable.value,
            placeholder = { Text(authUiState.user?.displayName.toString()) },
        )

        Button(onClick = { isEditable.value = !isEditable.value }) {
            Text(
                text = if (isEditable.value) "Cancel" else "Edit",
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Button(onClick = {
            authenticationViewModel.changeUsername(username.value)
        }) {
            Text(text = "Update Username", color = MaterialTheme.colorScheme.secondary)
        }
    }
}