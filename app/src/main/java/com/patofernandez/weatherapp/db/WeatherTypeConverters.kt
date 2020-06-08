package com.patofernandez.weatherapp.db

import androidx.room.TypeConverter
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.vo.City
import com.patofernandez.weatherapp.vo.CityDay
import com.patofernandez.weatherapp.vo.DayHour
import com.patofernandez.weatherapp.vo.LocationWeather

object WeatherTypeConverters {
    @TypeConverter
    @JvmStatic
    fun forecastResponseToLocationWeather(weatherForecastApiResponse: WeatherForecastApiResponse): LocationWeather {
        return LocationWeather(
            id = null,
            city = weatherForecastApiResponse.city?.name?: "",
            country = weatherForecastApiResponse.city?.country?: "",
            lat = weatherForecastApiResponse.city?.coord?.latitude?: .0,
            lng = weatherForecastApiResponse.city?.coord?.longitude?: .0,
            date = FormatUtils.formatedDate(weatherForecastApiResponse.list.first().date),
            temp = FormatUtils.formatedKelvinToCelsius(weatherForecastApiResponse.list.first().main?.temp)?: "",
            tempMax = FormatUtils.formatedKelvinToCelsius(weatherForecastApiResponse.list.first().main?.tempMax)?: "",
            tempMin = FormatUtils.formatedKelvinToCelsius(weatherForecastApiResponse.list.first().main?.tempMin)?: "",
            feelsLike = FormatUtils.formatedKelvinToCelsius(weatherForecastApiResponse.list.first().main?.feelsLike)?: "",
            pressure = (weatherForecastApiResponse.list.first().main?.pressure?.toString()?: "").plus("hpa"),
            humidity = (weatherForecastApiResponse.list.first().main?.humidity?.toString()?: "").plus("%"),
            weatherMain = weatherForecastApiResponse.list.first().weather.first()?.main?: "",
            iconUrl = "https://openweathermap.org/img/wn/${weatherForecastApiResponse.list.first().weather.first()?.icon?: "04d"}@4x.png",
            clouds = (weatherForecastApiResponse.list.first().clouds?.all?.toString()?: "").plus("%"),
            windSpeed = (weatherForecastApiResponse.list.first().wind?.speed?.toString()?: "").plus("Km/h")
        )
    }
    @TypeConverter
    @JvmStatic
    fun forecastResponseToCity(response: WeatherForecastApiResponse): City {
        return City().apply {
            city = response.city?.name ?: ""
            country = response.city?.country ?: ""
            lat = response.city?.coord?.latitude ?: .0
            lng = response.city?.coord?.longitude ?: .0
            val hours = response.list
                .map { hourOfDay->
                    DayHour().apply {
                        date = FormatUtils.formatedDate(hourOfDay.date)
                        hour = FormatUtils.formatedHour(hourOfDay.date)
                        temp = FormatUtils.formatedKelvinToCelsius(hourOfDay.main?.temp) ?: ""
                        tempMax = FormatUtils.formatedKelvinToCelsius(hourOfDay.main?.tempMax) ?: ""
                        tempMin = FormatUtils.formatedKelvinToCelsius(hourOfDay.main?.tempMin) ?: ""
                        feelsLike = FormatUtils.formatedKelvinToCelsius(hourOfDay.main?.feelsLike) ?: ""
                        pressure = (hourOfDay.main?.pressure?.toString() ?: "").plus(" hpa")
                        humidity = (hourOfDay.main?.humidity?.toString() ?: "").plus(" %")
                        weatherMain = hourOfDay.weather.first()?.main ?: ""
                        iconUrl = "https://openweathermap.org/img/wn/${hourOfDay.weather.first()?.icon ?: "04d"}@4x.png"
                        clouds = (hourOfDay.clouds?.all?.toString() ?: "").plus(" %")
                    }
                }
            hours
                .distinctBy { it.date }
                .forEach { item ->
                    days.add(
                        CityDay().apply {
                            date = item.date
                            day = item.date.split(" ").first()
                        }
                    )
                }
            days.forEach { cityDay ->
                cityDay.hours = hours.filter { cityDay.date == it.date } as ArrayList<DayHour>
            }
        }
    }
}
