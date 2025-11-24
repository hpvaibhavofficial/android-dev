package com.example.weatherapi


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    val temperature = MutableLiveData<String>()

    fun loadWeather(city: String) {
        viewModelScope.launch {
            val response = ApiClient.api.getCurrentWeather(
                apiKey = "b08091b2481c4cfa90561514250608",
                query = city
            )

            temperature.postValue("${response.current.temp_c} °C")
        }
    }
}
