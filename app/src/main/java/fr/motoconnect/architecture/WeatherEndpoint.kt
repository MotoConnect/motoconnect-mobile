package fr.motoconnect.architecture

import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherEndpoint {

    @GET("/")
    suspend fun getCurrentWeather(
        @Query(value = "lat") lat: String,
        @Query(value = "long") long: String,
        @Query(value = "appid") apiKey: String
    ): String

}