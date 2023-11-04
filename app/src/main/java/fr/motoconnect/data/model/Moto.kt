package fr.motoconnect.data.model

data class Moto(
    var name: String,
    var backTirePressure: Int = 1000,
    var frontTirePressure: Int = 1000,
    var brakeFluid: Int = 20000,
    var chainLubrication: Int = 250,
    var distance: Int = 0,
    var totalJourney: Int = 0,
    var current: Boolean = false,
    var journeys: List<Journey>? = null
)