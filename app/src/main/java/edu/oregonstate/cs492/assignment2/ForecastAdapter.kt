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
import java.text.SimpleDateFormat
import java.util.*

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
        private val timeTV: TextView = view.findViewById(R.id.tv_time)
        private lateinit var currentForecastPeriod: ForecastPeriod


        fun bind(forecastPeriod: ForecastPeriod) {
            currentForecastPeriod = forecastPeriod

            // Parsing and formatting date time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(forecastPeriod.dateTime)

            // Extracting month and day
            val cal = Calendar.getInstance()
            cal.time = date ?: Date()
            val month = cal.get(Calendar.MONTH) + 1 // Adding 1 because months start from 0
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val hour = cal.get(Calendar.HOUR)
            val minute = cal.get(Calendar.MINUTE)
            val amPm = if (cal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
            val timeIn12HourFormat = "${if (hour == 0) 12 else hour}:${
                if (minute < 10) "0$minute" else minute
            } $amPm"


            // Displaying parsed date information
            monthTV.text = getMonthName(month)
            dayTV.text = day.toString()
            timeTV.text = timeIn12HourFormat
            highTempTV.text = forecastPeriod.main.max.toString() + "°F"
            lowTempTV.text = forecastPeriod.main.min.toString() + "°F"
            popTV.text = (forecastPeriod.pop * 100.0).toInt().toString() + "% precip."
            shortDescTV.text = forecastPeriod.weather[0].description.capitalize()
        }
        private fun getMonthName(month: Int): String {
            val monthNames = arrayOf(
                "Jan", "Feb", "March", "April", "May", "June",
                "July", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            return monthNames[month - 1] // Adjusted to match the array index (months start from 0)
        }
    }
}