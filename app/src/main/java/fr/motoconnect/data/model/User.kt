package fr.motoconnect.data.model

data class User(
    val id: String,
    val caseId: Int,
    val language: String,
    val motos: List<Moto>
)