package fr.motoconnect.data.model

data class Moto(
    val id: String,
    val backTirePressure: Int,
    val frontTirePressure: Int,
    val brakeFluid: Int,
    val chainLubrication: Int,
    val distance: Int,
    val name: String,
    val totalJourney: Int,
    val journeys: List<Journey>
)