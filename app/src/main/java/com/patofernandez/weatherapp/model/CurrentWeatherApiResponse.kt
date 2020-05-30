package com.patofernandez.weatherapp.model

class CurrentWeatherApiResponse {
    var coord: Coordinates? = null              //{lat=37.42, lon = -122.08
    var weather: List<Weather?> = ArrayList()   //{description: clear sky, icon: 01n, id: 800, main: clear}
    var base: String = ""                       //stations
    var main: MainWeather? = null               //{feels_like: 289.09, humidity: 68.0, pressure: 1012.0, temp: 291.84, temp_max: 294.26, temp_min: 289.26}
    var visibility: Double = .0                 //16093.0
    var wind: Wind? = null                      //{deg: 330.0, speed: 5.1}
    var clouds: Clouds? = null                  //all = 5.0
    var rain: Rain? = null                      //null
    var snow: Snow? = null                      //null
    var dt: Long = 0L                           //1.590724537E9
    var sys: WeatherSys? = null                 //{country: US, sunrise: 1590670238, sunshine: 1590722465, type: 1}
    var timezone: Long = 0L                     //-25200
    var id: Long = 0L                           //5375480
    var name: String = ""                       //Mountain View
    var cod: Long = 0L                          //200
}