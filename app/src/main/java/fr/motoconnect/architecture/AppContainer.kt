package fr.motoconnect.architecture;

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {}

class DefaultAppContainer: AppContainer {

    private val baseUrl = "https://api.openweathermap.org/data/2.5/weather"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitWeatherEndpoint: WeatherEndpoint by lazy {
        retrofit.create(WeatherEndpoint::class.java)
    }
}