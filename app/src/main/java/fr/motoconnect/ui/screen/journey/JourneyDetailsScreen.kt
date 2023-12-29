package fr.motoconnect.ui.screen.journey

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.ui.component.Loading
import fr.motoconnect.viewmodel.JourneyDetailsViewModel

@Composable
fun JourneyDetailsScreen(
    journeyId: String?,
    navController: NavController
) {

    val journeyDetailsViewModel: JourneyDetailsViewModel = viewModel()
    val journeyDetailsUIState by journeyDetailsViewModel.journeyDetailsUiState.collectAsState()

    LaunchedEffect(journeyId) {
        journeyDetailsViewModel.getJourney(journeyId = journeyId!!)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = {
                    Text(text = "Journey details")
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
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                journeyDetailsUIState.isLoading -> {
                    Loading()
                }

                journeyDetailsUIState.journey == null -> {
                    Text(text = "Journey not found")
                }

                else -> {
                    Log.d(
                        "TAG",
                        "JourneyDetailsScreen: " + journeyDetailsUIState.journey.toString()
                    )

                    /*
                    Button(onClick = { journeyDetailsViewModel.addJourney() }) {
                        Text(text = "Click me")
                    }
                    */
                    JourneyDetailsContent(journeyDetailsUIState.journey!!)
                }
            }
        }

    }

}

@Composable
fun JourneyDetailsContent(
    journey: JourneyObject
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            calculateMidPoint(
                LatLng(
                    journey.points.first().geoPoint.latitude,
                    journey.points.first().geoPoint.longitude
                ), LatLng(
                    journey.points.last().geoPoint.latitude,
                    journey.points.last().geoPoint.longitude
                )
            ), 16f
        )
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
    ) {
        DrawPolyline(journey)
    }

}

@Composable
fun DrawPolyline(
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
}

fun getColorBasedOnSpeed(speed: Long): Color {

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

fun calculateMidPoint(point1: LatLng, point2: LatLng): LatLng {
    // Convert latitude and longitude to radians
    val lat1 = Math.toRadians(point1.latitude)
    val lon1 = Math.toRadians(point1.longitude)
    val lat2 = Math.toRadians(point2.latitude)
    val lon2 = Math.toRadians(point2.longitude)

    // Calculate components Bx and By for simplifying expressions
    val Bx = Math.cos(lat2) * Math.cos(lon2 - lon1)
    val By = Math.cos(lat2) * Math.sin(lon2 - lon1)

    // Calculate latitude of the midpoint (lat3)
    val lat3 = Math.atan2(
        Math.sin(lat1) + Math.sin(lat2),
        Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By)
    )

    // Calculate longitude of the midpoint (lon3)
    val lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx)

    // Convert results back to degrees
    val latitude = Math.toDegrees(lat3)
    val longitude = Math.toDegrees(lon3)

    return LatLng(latitude, longitude)
}

