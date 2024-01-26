package fr.motoconnect.ui.screen.profile.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.viewmodel.AuthenticationViewModel

enum class ButtonStateUsername { EDIT, UPDATE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeUsernameComponent(
    authenticationViewModel: AuthenticationViewModel
) {
    val username = remember { mutableStateOf("") }

    val isEditable = remember { mutableStateOf(false) }

    val authUiState by authenticationViewModel.authUiState.collectAsState()

    val isUsernameError = remember { mutableStateOf(false) }

    val buttonStateUsername = remember { mutableStateOf(ButtonStateUsername.EDIT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(text = "Username", color = MaterialTheme.colorScheme.secondary) },
            readOnly = !isEditable.value,
            isError = isUsernameError.value,
            placeholder = { Text(authUiState.user?.displayName.toString()) },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 15.dp)
        ) {
            Button(
                onClick = {
                    when (buttonStateUsername.value) {
                        ButtonStateUsername.EDIT -> {
                            isEditable.value = !isEditable.value
                            buttonStateUsername.value = ButtonStateUsername.UPDATE
                        }
                        ButtonStateUsername.UPDATE -> {
                            if(username.value.isEmpty()) {
                                isUsernameError.value = true
                            } else {
                                isUsernameError.value = false
                                authenticationViewModel.changeUsername(username.value)
                                isEditable.value = false
                                buttonStateUsername.value = ButtonStateUsername.EDIT
                            }
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Text(
                    text = when (buttonStateUsername.value) {
                        ButtonStateUsername.EDIT -> "Edit"
                        ButtonStateUsername.UPDATE -> "Update Username"
                    }
                )
            }

        }
        if (isUsernameError.value) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Username cannot be empty",
                color = MaterialTheme.colorScheme.error,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}