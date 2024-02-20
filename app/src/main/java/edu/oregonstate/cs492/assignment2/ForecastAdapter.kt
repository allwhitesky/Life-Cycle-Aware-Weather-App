package edu.oregonstate.cs492.assignment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.oregonstate.cs492.assignment2.data.ForecastPeriod
import java.util.Calendar
import java.util.Locale

class ForecastAdapter() :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    private var forecastPeriods = listOf<ForecastPeriod>()

    fun updateForecastList(newForecastList: List<ForecastPeriod>?) {
        notifyItemRangeRemoved(0, forecastPeriods.size)
        forecastPeriods = newForecastList ?: listOf()
        notifyItemRangeInserted(0, forecastPeriods.size)
    }

    override fun getItemCount() = forecastPeriods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastPeriods[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val monthTV: TextView = view.findViewById(R.id.tv_month)
        private val dayTV: TextView = view.findViewById(R.id.tv_day)
        private val highTempTV: TextView = view.findViewById(R.id.tv_high_temp)
        private val lowTempTV: TextView = view.findViewById(R.id.tv_low_temp)
        private val shortDescTV: TextView = view.findViewById(R.id.tv_short_description)
        private val popTV: TextView = view.findViewById(R.id.tv_pop)
        private lateinit var currentForecastPeriod: ForecastPeriod


        fun bind(forecastPeriod: ForecastPeriod) {
            currentForecastPeriod = forecastPeriod

//            val cal = Calendar.getInstance()
//            cal.set(forecastPeriod.year, forecastPeriod.month, forecastPeriod.day)
//
//            monthTV.text = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
//            dayTV.text = cal.get(Calendar.DAY_OF_MONTH).toString()
            highTempTV.text = forecastPeriod.main.max.toString() + "°F"
            lowTempTV.text = forecastPeriod.main.min.toString() + "°F"
            popTV.text = (forecastPeriod.pop * 100.0).toInt().toString() + "% precip."
            shortDescTV.text = forecastPeriod.weather[0].description
        }
    }
}