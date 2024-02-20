package edu.oregonstate.cs492.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import edu.oregonstate.cs492.assignment2.data.ForecastPeriod
import edu.oregonstate.cs492.assignment2.data.ForecastSearchResults
import edu.oregonstate.cs492.assignment2.data.ForecastService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val forecastService = ForecastService.create()
    private lateinit var adapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forecastListRV = findViewById<RecyclerView>(R.id.rv_forecast_list)
        forecastListRV.layoutManager = LinearLayoutManager(this)
        forecastListRV.setHasFixedSize(true)

        adapter = ForecastAdapter()
        forecastListRV.adapter = adapter

        doForecastSearch("44.5646", "123.2620", "imperial", "079f4b960217156334cd84b43d164fe7")

//        forecastListRV.adapter = ForecastAdapter(forecastDataItems)
    }

    private fun doForecastSearch(lat: String, lon: String, unit: String, appid: String) {
        forecastService.searchForecast(lat, lon, appid).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("MainActivity", "Status code: ${response.code()}")
                Log.d("MainActivity", "Response Body: ${response.body()}")
                if (response.isSuccessful){
                    val moshi = Moshi.Builder().build()
                    val jsonAdapter: JsonAdapter<ForecastSearchResults> =
                        moshi.adapter(ForecastSearchResults::class.java)
                    val searchResults = jsonAdapter.fromJson((response.body())) //this is the results of the API call
                    Log.d("MainActivity", "searchResults: $searchResults")
                    adapter.updateForecastList(searchResults?.list)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("MainActivity", "Error making API call: ${t.message}")
            }

        })

    }

}