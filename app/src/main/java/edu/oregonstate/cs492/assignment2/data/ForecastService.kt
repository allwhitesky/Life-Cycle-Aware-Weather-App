package edu.oregonstate.cs492.assignment2.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.moshi.MoshiConverterFactory

interface ForecastService {

    @GET("data/2.5/forecast")
    suspend fun searchForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = "69c61140a703c038ddb9c5366eaa5849",
        @Query("units") unit: String = "imperial"
    ): Response<ForecastSearchResults>

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"

        fun create(): ForecastService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ForecastService::class.java)
        }
    }
}

