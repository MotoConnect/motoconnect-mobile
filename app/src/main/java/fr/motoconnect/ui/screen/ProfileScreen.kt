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
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.edit

//Temporaire?
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
    var switchStateLocation by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "switchStateLocation",
                false
            )
        )
    }
    var switchStateNotification by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "switchStateNotification",
                false
            )
        )
    }
    var switchStateDisplay by rememberSaveable {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "switchStateDisplay",
                false
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.broken_white))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Screen Profile",
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
                        .height(200.dp)
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = "Profil",
                        color = colorResource(R.color.broken_white),
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
                                color = colorResource(R.color.broken_white),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = userData.userId,
                                color = colorResource(R.color.broken_white),
                                fontSize = 15.sp,
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
                    .height(200.dp)
                    .fillMaxWidth()
            )
            {
                Text(
                    text = "Préférences",
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
                            text = "Location",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Switch(
                            checked = switchStateLocation,
                            onCheckedChange = {
                                switchStateLocation = it
                                sharedPreferences.edit {
                                    putBoolean("switchStateLocation", it)
                                    apply()
                                }
                            },
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
                    }
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Notification",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Switch(
                            checked = switchStateNotification,
                            onCheckedChange = {
                                switchStateNotification = it
                                sharedPreferences.edit {
                                    putBoolean("switchStateNotification", it)
                                    apply()
                                }
                            },
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
                    }
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Display",
                            color = colorResource(R.color.broken_white),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
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
                            }
                        )
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
                    .height(200.dp)
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = "Disconnection",
                        color = colorResource(R.color.broken_white),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Button(onClick = onSignOut) {
                        Text(
                            text = "Log Out",
                            color = colorResource(R.color.broken_white)
                        )
                    }
                }
            }
        }
        item {

        }
    }
}