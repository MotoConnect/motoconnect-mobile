package fr.motoconnect.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.UserData
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.content.edit
import firebase.com.protolitewrapper.BuildConfig
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}

fun onAccountDelete() {
    //TODO
}

fun onAppVersion(context: Context) {
    Toast.makeText(
        context,
        "Version Name : " + BuildConfig.VERSION_NAME + "\n" + "Version Code : " + BuildConfig.VERSION_CODE.toString(),
        Toast.LENGTH_LONG
    ).show()
}

@OptIn(ExperimentalPermissionsApi::class)
fun askNotification(notificationPermission: PermissionState){
    if (!notificationPermission.status.isGranted) {
            Log.d("NOTIF","Demande les notif")
            notificationPermission.launchPermissionRequest()

    } else {
        Log.d("NOTIF","A accepté les notif")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun askLocation(locationPermission: PermissionState){
    if (!locationPermission.status.isGranted) {
        Log.d("NOTIF","Demande la localisation")
        locationPermission.launchPermissionRequest()

    } else {
        Log.d("NOTIF","A accepté la localisation")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = getSharedPreferences(context)
    var switchStateDisplay by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "switchStateDisplay",
                false
            )
        )
    }
    val notificationPermission = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    )
    val locationPermission = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.broken_white))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Settings",
                color = colorResource(R.color.broken_white),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(color = colorResource(R.color.purple), shape = CircleShape)
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if (userData?.username != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.purple),
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ), modifier = Modifier
                        .height(210.dp)
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = "Profile",
                        color = colorResource(R.color.broken_white),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(14.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            val painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = userData.profilePictureUrl)
                                    .build()
                            )
                            Image(
                                painter = painter,
                                contentDescription = "Profile picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(shape = CircleShape)
                            )
                        }
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = "Username :",
                                color = colorResource(R.color.broken_white),
                                fontSize = 15.sp,
                                textDecoration = TextDecoration.Underline
                            )
                            Text(
                                text = userData.username,
                                color = colorResource(R.color.broken_white),
                                fontSize = 14.sp,
                            )
                            Text(
                                text = "Email :",
                                color = colorResource(R.color.broken_white),
                                fontSize = 15.sp,
                                textDecoration = TextDecoration.Underline
                            )
                            Text(
                                text = userData.email!!,
                                color = colorResource(R.color.broken_white),
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.purple),
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "Preferences",
                    color = colorResource(R.color.broken_white),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(14.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Location",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if(!locationPermission.status.isGranted){
                            Button(
                                onClick = { askLocation(locationPermission) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.brown),
                                )

                            ) {
                                Text(
                                    text = "Activate location",
                                    color = colorResource(R.color.broken_white)
                                )
                            }
                        }
                        else{
                            Text(
                                text = "Location activated",
                                color = colorResource(R.color.broken_white)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Notification",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if(!notificationPermission.status.isGranted){
                            Button(
                                onClick = { askNotification(notificationPermission) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.brown),
                                )

                            ) {
                                Text(
                                    text = "Activate notification",
                                    color = colorResource(R.color.broken_white),
                                )
                            }
                        }
                        else{
                            Text(
                                text = "Notification activated",
                                color = colorResource(R.color.broken_white)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Text(
                        text = "Display",
                        color = colorResource(R.color.broken_white),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Switch(
                        checked = switchStateDisplay,
                        onCheckedChange = {
                            switchStateDisplay = it
                            sharedPreferences.edit {
                                putBoolean("switchStateDisplay", it)
                                apply()
                            }
                        },
                        thumbContent = if (switchStateDisplay) {
                            {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorResource(R.color.broken_white),
                            checkedTrackColor = colorResource(R.color.brown),
                            uncheckedThumbColor = colorResource(R.color.broken_white),
                            uncheckedTrackColor = colorResource(R.color.purple),
                        )
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.purple),
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "Actions",
                    color = colorResource(R.color.broken_white),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(14.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Disconnection",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = onSignOut,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.brown),
                            )

                        ) {
                            Text(
                                text = "Logout",
                                color = colorResource(R.color.broken_white)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            text = "Account deletion",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = { onAccountDelete() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.brown),
                            )
                        ) {
                            Text(
                                text = "Delete",
                                color = colorResource(R.color.broken_white)
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.purple),
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "About",
                    color = colorResource(R.color.broken_white),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(14.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.brown),
                        )

                    ) {
                        Text(
                            text = "Application infos",
                            color = colorResource(R.color.broken_white)
                        )
                    }
                    Button(
                        onClick = { onAppVersion(context) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.brown),
                        )
                    ) {
                        Text(
                            text = "Application version",
                            color = colorResource(R.color.broken_white)
                        )
                    }
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Row (horizontalArrangement = Arrangement.SpaceEvenly){
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "App Info",
                        color = colorResource(R.color.broken_white),
                    )
                }

            },
            text = {
                Text(
                    text = "This is the app info.",
                    color = colorResource(R.color.broken_white)
                )
            },
            backgroundColor = colorResource(R.color.purple),
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.brown),
                    ),
                    modifier = Modifier.padding(0.dp,0.dp,10.dp,10.dp),
                ) {
                    Text(
                        text = "Close",
                        color = colorResource(R.color.broken_white)
                    )
                }
            }
        )
    }
}