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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.UserData
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.width
import androidx.core.content.edit

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}


@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = getSharedPreferences(context)
    var switchStateLocation by rememberSaveable { mutableStateOf(sharedPreferences.getBoolean("switchStateLocation", false)) }
    var switchStateNotification by rememberSaveable { mutableStateOf(sharedPreferences.getBoolean("switchStateNotification", false)) }
    var switchStateDisplay by rememberSaveable { mutableStateOf(sharedPreferences.getBoolean("switchStateDisplay", false)) }


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(userData?.username != null) {
            Card (
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth())
            {
                Text(
                    text = "Profil",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(14.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.moto),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.padding(14.dp)
                    )
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = userData.username,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = userData.userId,
                            fontSize = 15.sp,
                            )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card (
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth())
            {
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(14.dp)
                        .fillMaxWidth()){
                        Switch(
                            checked = switchStateLocation,
                            onCheckedChange = { switchStateLocation = it
                                sharedPreferences.edit {
                                    putBoolean("switchStateLocation", it)
                                    apply()
                                }},
                            thumbContent = if (switchStateLocation) {
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
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Switch(
                            checked = switchStateNotification,
                            onCheckedChange = { switchStateNotification = it
                                sharedPreferences.edit {
                                    putBoolean("switchStateNotification", it)
                                    apply()
                                }},
                            thumbContent = if (switchStateNotification) {
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
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Switch(
                            checked = switchStateDisplay,
                            onCheckedChange = { switchStateDisplay = it
                                sharedPreferences.edit {
                                    putBoolean("switchStateDisplay", it)
                                    apply()
                                }},
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
                            }
                        )
                }
            }
        }
        Button(onClick = onSignOut) {
            Text(text = "Log Out")
        }
    }
}