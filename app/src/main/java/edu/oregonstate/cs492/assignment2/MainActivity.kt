package edu.oregonstate.cs492.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
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
    private val viewModel: ForecastViewModel by viewModels()

//    private val forecastService = ForecastService.create()
    private lateinit var adapter: ForecastAdapter
    private lateinit var searchErrorTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forecastListRV = findViewById<RecyclerView>(R.id.rv_forecast_list)
        forecastListRV.layoutManager = LinearLayoutManager(this)
        forecastListRV.setHasFixedSize(true)
        searchErrorTV = findViewById(R.id.tv_search_error)

        adapter = ForecastAdapter()
        forecastListRV.adapter = adapter

        viewModel.searchResults.observe(this) {
            searchResults -> adapter.updateForecastList(searchResults)
        }

        viewModel.loadingStatus.observe(this) {
                loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.ERROR -> {
                    forecastListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    forecastListRV.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.searchResults.observe(this) {
            error -> searchErrorTV.text = getString(
                R.string.api_error,
                error
            )
        }
        viewModel.loadSearchResults("44.5382656", "-123.2502784",  "079f4b960217156334cd84b43d164fe7","imperial")
//        doForecastSearch("44.5382656", "-123.2502784",  "079f4b960217156334cd84b43d164fe7","imperial")
    }
}


//    private fun doForecastSearch(lat: String, lon: String, appid: String, unit: String) {
//        forecastService.searchForecast(lat, lon, appid).enqueue(object : Callback<ForecastSearchResults> {
//            override fun onResponse(call: Call<ForecastSearchResults>, response: Response<ForecastSearchResults>) {
//                Log.d("MainActivity", "Status code: ${response.code()}")
//                Log.d("MainActivity", "Response Body: ${response.body()}")
//                if (response.isSuccessful){
//                    adapter.updateForecastList(response.body()?.list)
//                }
//            }
//
//            override fun onFailure(call: Call<ForecastSearchResults>, t: Throwable) {
//                Log.d("MainActivity", "Error making API call: ${t.message}")
//            }
//        })
//    }
