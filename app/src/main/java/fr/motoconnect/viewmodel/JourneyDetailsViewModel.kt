package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.model.PointObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import kotlin.random.Random

class JourneyDetailsViewModel : ViewModel() {

    val TAG = "JourneyDetailsViewModel"


    private val _journeyDetailsUiState = MutableStateFlow(JourneyDetailsUIState())
    val journeyDetailsUiState: StateFlow<JourneyDetailsUIState> =
        _journeyDetailsUiState.asStateFlow()

    val db = Firebase.firestore
    val auth = Firebase.auth

    fun addJourney() {
        val points = listOf(
            LatLng(49.8429023, 3.2934764),
            LatLng(49.8437271, 3.2920876),
            LatLng(49.8423261, 3.2944577),
            LatLng(49.8418082, 3.2953073),
            LatLng(49.8414761, 3.2959725),
            LatLng(49.8409571, 3.2969917),
            LatLng(49.8403275, 3.298204),
            LatLng(49.8397348, 3.2993252),
            LatLng(49.839244, 3.3000863),
            LatLng(49.839563, 3.301333),
        )
        val now = Timestamp.now()
        var speed = 5
        var currentTime = now.seconds
        points.map { point ->
            val instant = Instant.ofEpochSecond(currentTime)
            val newInstant = instant.plusSeconds(3)
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("journeys")
                .document("0y7IhcutyFuCVDZClPbA")
                .collection("points")
                .add(
                    PointObject(
                        geoPoint = GeoPoint(point.latitude, point.longitude),
                        speed = speed.toLong(),
                        time = Timestamp(newInstant.epochSecond, newInstant.nano),
                        tilt = Random.nextInt(0, 35).toLong(),
                    )
                )
            speed += 5
            currentTime += 3
        }

    }


    suspend fun getJourney(journeyId: String) {
        val dbref = db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("journeys")
            .document(journeyId)

        try {
            val document = dbref.get().await()

            if (document != null) {
                val pointsList = coroutineScope {
                    async(Dispatchers.IO) {
                        db.collection("users")
                            .document(auth.currentUser?.uid.toString())
                            .collection("journeys")
                            .document(journeyId)
                            .collection("points")
                            .orderBy("time")
                            .get().await()
                            .map { document ->
                                PointObject(
                                    geoPoint = document.get("geoPoint") as GeoPoint,
                                    speed = document.get("speed") as Long,
                                    time = document.get("time") as Timestamp,
                                    tilt = document.get("tilt") as Long,
                                )
                            }
                    }
                }.await()

                val journey = JourneyObject(
                    id = document.id,
                    startDateTime = document.get("startDateTime") as Timestamp?,
                    distance = document.get("distance") as Long?,
                    duration = document.get("duration") as Long?,
                    endDateTime = document.get("endDateTime") as Timestamp?,
                    averageSpeed = document.get("averageSpeed") as Long?,
                    finished = document.get("finished") as Boolean?,
                    points = pointsList,
                )
                _journeyDetailsUiState.value = JourneyDetailsUIState(
                    isLoading = false,
                    journey = journey,
                    errorMsg = null,
                    currentPoint = journey.points.first(),
                )
            }
        } catch (exception: Exception) {
            _journeyDetailsUiState.value = JourneyDetailsUIState(
                isLoading = false,
                journey = null,
                errorMsg = exception.message,
                currentPoint = null,
            )
            Log.w(TAG, "Error getting documents: ", exception)
        }
    }

    fun setCurrentPoint(point: PointObject) {
        _journeyDetailsUiState.value = _journeyDetailsUiState.value.copy(
            currentPoint = point,
        )
    }

    fun setPlayerState(state: JourneyPlayerState) {
        _journeyDetailsUiState.value = _journeyDetailsUiState.value.copy(playerState = state)
    }

    fun getPlayerState(): JourneyPlayerState {
        return _journeyDetailsUiState.value.playerState
    }
}

data class JourneyDetailsUIState(
    var isLoading: Boolean = true,
    var journey: JourneyObject? = null,
    var errorMsg: String? = null,
    val currentPoint: PointObject? = null,
    val playerState: JourneyPlayerState = JourneyPlayerState.STOPPED,
)

enum class JourneyPlayerState {
    PLAYING,
    STOPPED,
    PAUSED,
}