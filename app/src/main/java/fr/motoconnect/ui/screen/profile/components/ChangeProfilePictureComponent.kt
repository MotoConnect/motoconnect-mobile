package fr.motoconnect.ui.screen.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import fr.motoconnect.viewmodel.AuthenticationViewModel

@Composable
fun ChangeProfilePictureComponent(authenticationViewModel: AuthenticationViewModel, auth: FirebaseAuth) {

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val storage = FirebaseStorage.getInstance()
    val imageRef = storage.reference.child( auth.currentUser!!.uid + "/profilePicture")

    LaunchedEffect(Unit) {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUri = uri
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = null
                imageUri = it
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp, 100.dp)
            )
        }

        TextButton(
            onClick = {
                galleryLauncher.launch("image/*")
            }
        ) {
            Text(
                text = "Pick image"
            )
        }

        Button(onClick = {
            authenticationViewModel.changeProfilePicture(imageUri!!)
        }) {
            Text(text = "Update Profile Picture", color = MaterialTheme.colorScheme.secondary)
        }
    }

}