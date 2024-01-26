package fr.motoconnect.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.motoconnect.ui.navigation.MotoConnectNavigationRoutes
import fr.motoconnect.ui.screen.profile.components.ChangePasswordComponent
import fr.motoconnect.ui.screen.profile.components.ChangeProfilePictureComponent
import fr.motoconnect.ui.screen.profile.components.ChangeUsernameComponent
import fr.motoconnect.viewmodel.AuthenticationViewModel

@Composable
fun ModifyProfileScreen(
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController,
    auth: FirebaseAuth
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Modify Profile Picture",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp),
            color= MaterialTheme.colorScheme.tertiary
        )

        ChangeProfilePictureComponent(authenticationViewModel = authenticationViewModel, auth = auth)

        Text(
            text = "Modify Username",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp),
            color= MaterialTheme.colorScheme.tertiary
        )

        ChangeUsernameComponent(authenticationViewModel = authenticationViewModel)

        Text(
            text = "Modify Password",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(14.dp),
            color= MaterialTheme.colorScheme.tertiary
        )

        ChangePasswordComponent(authenticationViewModel = authenticationViewModel)

        Button(
            onClick = {
                navController.navigate(MotoConnectNavigationRoutes.Profile.name)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.secondary,
            )
        ) {
            Text(text = "Return")
        }

    }

}

