package fr.motoconnect.data.model

data class Moto(
    val id: String,
    val backTirePressure: Int = 1000,
    val frontTirePressure: Int = 1000,
    val brakeFluid: Int = 20000,
    val chainLubrication: Int = 250,
    val distance: Int = 0,
    val name: String,
    val totalJourney: Int = 0,
    val journeys: List<Journey>
)