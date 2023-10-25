package fr.motoconnect.data.model

import com.google.firebase.Timestamp

data class Point(
    val timestamp: Timestamp,
    val speed: Int,
    val tilt: Int
)