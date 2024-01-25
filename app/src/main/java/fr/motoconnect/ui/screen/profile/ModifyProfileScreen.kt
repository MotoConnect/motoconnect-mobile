package fr.motoconnect.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.motoconnect.ui.screen.profile.components.ChangePasswordComponent
import fr.motoconnect.ui.screen.profile.components.ChangeProfilePictureComponent
import fr.motoconnect.ui.screen.profile.components.ChangeUsernameComponent
import fr.motoconnect.viewmodel.AuthenticationViewModel

@Composable
fun ModifyProfileScreen(authenticationViewModel: AuthenticationViewModel, navController: NavController, auth: FirebaseAuth) {

    //Text(text ="ModifyProfileScreen")

    Column {

        //Design en cours de révision

        ChangeProfilePictureComponent(authenticationViewModel = authenticationViewModel, auth = auth)

        ChangeUsernameComponent(authenticationViewModel = authenticationViewModel)

        ChangePasswordComponent(authenticationViewModel = authenticationViewModel)

    }

}

