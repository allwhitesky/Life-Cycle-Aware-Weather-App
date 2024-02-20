package edu.oregonstate.cs492.assignment2.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastPeriod(
    val main: Main,
    val weather: List<Weather>,
    @Json(name = "dt") val time: Int,
    val pop: Int
)

@JsonClass(generateAdapter = true)
data class Weather(
    val description: String
)

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp_min") val min: Double,
    @Json(name = "temp_max") val max: Double,
)