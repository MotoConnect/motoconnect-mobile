package fr.motoconnect.ui.screen.journey

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.model.PointObject
import fr.motoconnect.ui.component.Loading
import fr.motoconnect.ui.utils.MarkerCustomUtils
import fr.motoconnect.ui.utils.TimestampUtils
import fr.motoconnect.viewmodel.JourneyDetailsViewModel
import fr.motoconnect.viewmodel.JourneyPlayerState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun JourneyDetailsScreen(
    journeyId: String?,
    navController: NavController
) {

    val journeyDetailsViewModel: JourneyDetailsViewModel = viewModel()
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(journeyId) {
        journeyDetailsViewModel.getJourney(journeyId = journeyId!!)
    }

    val contentResolver = context.contentResolver

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/gpx+xml")) { selectedUri ->
            if (selectedUri != null) {
                contentResolver.openOutputStream(selectedUri)?.use {
                    val bytes = journeyDetailsViewModel.convertGpxAndReturnString().toByteArray()
                    it.write(bytes)
                }
            } else {
                Log.d("GenerateGpx", "Uri is null")
            }
        }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = {
                    when (journeyDetailsUIState.journey) {
                        null -> {
                            Text(text = stringResource(R.string.journey_details))
                        }

                        else -> {
                            Text(
                                text = stringResource(
                                    R.string.trajet_du, TimestampUtils().toDateString(
                                        journeyDetailsUIState.journey?.startDateTime!!
                                    )
                                ),
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .clip(RoundedCornerShape(10))
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                launcher.launch(
                                    "motoConnect-${
                                        TimestampUtils().toDateTimeString(
                                            journeyDetailsUIState.journey?.startDateTime!!
                                        )
                                    }.gpx"
                                )
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.export_as_gpx),
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                        DropdownMenuItem(
                            onClick = { expanded = false }
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {


            when {

                journeyDetailsUIState.isLoading -> {
                    Loading()
                }

                journeyDetailsUIState.journey == null -> {
                    Text(text = stringResource(R.string.journey_not_found))
                    //TODO: Add an illustration
                }

                else -> {

                    /*
                        Button(onClick = { journeyDetailsViewModel.addJourney() }) {
                            Text(text = "Click me")
                        }
                        */
                    JourneyDetailsContent(journeyDetailsUIState.journey!!, journeyDetailsViewModel)
                }
            }
        }
    }

}

@Composable
fun JourneyDetailsContent(
    journey: JourneyObject,
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    val context = LocalContext.current

    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = journeyDetailsUIState.journey?.points?.first()?.geoPoint?.let {
            CameraPosition.fromLatLngZoom(
                LatLng(
                    it.latitude,
                    it.longitude
                ), 15f
            )
        }!!
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isTrafficEnabled = false,
            mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                R.raw.map_style
            ),
        ),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
        ),
    ) {
        DrawJourney(journey)

        if (journeyDetailsUIState.currentPoint != null) {
            Marker(
                state = MarkerState(
                    LatLng(
                        journeyDetailsUIState.currentPoint!!.geoPoint.latitude,
                        journeyDetailsUIState.currentPoint!!.geoPoint.longitude
                    )
                ),
                icon = MarkerCustomUtils().bitmapDescriptorFromVector(
                    context,
                    R.drawable.moto_position
                ),
                anchor = Offset(0.5f, 0.5f)
            )
        }
    }
    BoxOnMap(journeyDetailsViewModel = journeyDetailsViewModel)

}

@Composable
fun BoxOnMap(
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PointInfoComponent(point = journeyDetailsUIState.currentPoint!!)
            SliderJourneyComponent(
                journeyDetailsViewModel = journeyDetailsViewModel
            )
        }

    }
}

@Composable
fun PointInfoComponent(
    point: PointObject
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.tertiary)
            .width(200.dp)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color(0xFF343333),
                        radius = 160f,
                    )
                },
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp,
            text = "${point.speed}\nkm/h",
            fontWeight = FontWeight.Bold
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.moto_front),
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .rotate(point.tilt.toFloat()),
            )
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = "${getTilt(point)}°",
                fontSize = 18.sp,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderJourneyComponent(
    journeyDetailsViewModel: JourneyDetailsViewModel
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = {
                togglePlayJourney(
                    journeyDetailsUIState.journey!!,
                    journeyDetailsViewModel,
                )
            },
            modifier = Modifier
                .background(Color(0xFF343333), RoundedCornerShape(50))
        ) {
            if (journeyDetailsUIState.playerState == JourneyPlayerState.PLAYING) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pause_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = TimestampUtils().toTime(journeyDetailsUIState.currentPoint?.time?.seconds!!),
            fontSize = 18.sp,
        )

        Slider(
            value = journeyDetailsUIState.currentPoint?.let {
                journeyDetailsUIState.journey?.points?.indexOf(
                    it
                )?.toFloat()!!
            } ?: 0f,
            onValueChange = {
                journeyDetailsViewModel.setCurrentPoint(
                    journeyDetailsUIState.journey?.points!![it.toInt()]
                )
                sliderPosition = it
            },
            valueRange = 0f..journeyDetailsUIState.journey?.points?.size?.toFloat()?.minus(1f)!!,
            steps = journeyDetailsUIState.journey?.points?.size?.minus(2)!!,
            thumb = {
                Image(
                    painter = painterResource(id = R.drawable.moto_position),
                    contentDescription = null,
                    Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
            },
            enabled = journeyDetailsUIState.playerState != JourneyPlayerState.PLAYING,
        )

    }
}

@Composable
fun DrawJourney(
    journey: JourneyObject
) {
    for (i in 0 until journey.points.size - 1) {
        Polyline(
            points = listOf(
                LatLng(
                    journey.points[i].geoPoint.latitude,
                    journey.points[i].geoPoint.longitude
                ),
                LatLng(
                    journey.points[i + 1].geoPoint.latitude,
                    journey.points[i + 1].geoPoint.longitude
                )
            ),
            color = getColorBasedOnSpeed(journey.points[i].speed),
            width = 15f,
        )
    }
    Marker(
        state = MarkerState(
            LatLng(
                journey.points.first().geoPoint.latitude,
                journey.points.first().geoPoint.longitude
            )
        ),
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),

        )
    Marker(
        state = MarkerState(
            LatLng(
                journey.points.last().geoPoint.latitude,
                journey.points.last().geoPoint.longitude
            )
        ),
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    )
}

private fun getTilt(
    point: PointObject,
): Long {
    return kotlin.math.abs(point.tilt)
}

private fun getColorBasedOnSpeed(speed: Long): Color {

    return when (speed) {
        in 0..20 -> Color(0xFF7dfa00)
        in 20..40 -> Color(0xFFa3f800)
        in 40..50 -> Color(0xFFdbf300)
        in 50..60 -> Color(0xFFf6d700)
        in 60..70 -> Color(0xFFffc900)
        in 70..80 -> Color(0xFFff8d00)
        in 80..90 -> Color(0xFFff6b00)
        else -> Color(0xFFfc4104)
    }

}


private fun togglePlayJourney(
    journey: JourneyObject,
    journeyDetailsViewModel: JourneyDetailsViewModel,
) {
    when (journeyDetailsViewModel.journeyDetailsUiState.value.playerState) {
        JourneyPlayerState.PLAYING -> {
            journeyDetailsViewModel.setPlayerState(JourneyPlayerState.PAUSED)
        }

        JourneyPlayerState.STOPPED -> {
            journeyDetailsViewModel.setPlayerState(JourneyPlayerState.PLAYING)
            playJourney(journey, journeyDetailsViewModel)
        }

        else -> {
            journeyDetailsViewModel.setPlayerState(JourneyPlayerState.PLAYING)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun playJourney(journey: JourneyObject, journeyDetailsViewModel: JourneyDetailsViewModel) {

    GlobalScope.launch {
        for (i in 0 until journey.points.size) {
            if (journeyDetailsViewModel.getPlayerState() == JourneyPlayerState.PAUSED) {
                while (journeyDetailsViewModel.getPlayerState() == JourneyPlayerState.PAUSED) {
                    kotlinx.coroutines.delay(100)
                }
            }

            journeyDetailsViewModel.setCurrentPoint(journey.points[i])
            kotlinx.coroutines.delay(1000)
        }

        journeyDetailsViewModel.setCurrentPoint(journey.points.first())
        journeyDetailsViewModel.setPlayerState(JourneyPlayerState.STOPPED)
    }


}