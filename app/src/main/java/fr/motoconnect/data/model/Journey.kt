package fr.motoconnect.data.model

import com.google.type.DateTime

data class Journey(
    val id: String,
    val date: DateTime,
    val points: List<Point>
)