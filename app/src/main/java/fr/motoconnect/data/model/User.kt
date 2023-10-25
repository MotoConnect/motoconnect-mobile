package fr.motoconnect.data.model

data class User(
    var id: String?,
    var caseId: Int? = null,
    var language: String = "fr",
    var motos: List<Moto>? = null
)