package com.example.weatherapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String    // e.g. "Delhi" or "28.6,77.2"
    ): WeatherResponse
}



object ApiClient {

    val api: WeatherApi = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)
}


data class WeatherResponse(
    val current: Current
)



data class Current(
    val temp_c: Double,


)



