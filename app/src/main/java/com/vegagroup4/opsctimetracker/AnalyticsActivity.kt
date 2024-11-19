package com.vegagroup4.opsctimetracker.User

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.vegagroup4.opsctimetracker.R
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var btnSaveHours: Button
    private lateinit var btnSelectDateRange: Button
    private lateinit var etHoursWorked: EditText
    private lateinit var tvSelectedDate: TextView
    private var hoursWorkedData: MutableList<Pair<String, Float>> = mutableListOf()
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        // Initialize views
        barChart = findViewById(R.id.barChart)
        btnSaveHours = findViewById(R.id.btnSaveHours)
        btnSelectDateRange = findViewById(R.id.btnSelectDateRange)
        etHoursWorked = findViewById(R.id.etHoursWorked)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)

        setupBarChart()

        btnSaveHours.setOnClickListener {
            saveHours()
        }

        btnSelectDateRange.setOnClickListener {
            selectDate()
        }
    }

    private fun saveHours() {
        if (selectedDate == null) {
            Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show()
            return
        }

        val hoursWorked = etHoursWorked.text.toString().toFloatOrNull()
        if (hoursWorked == null || hoursWorked < 0) {
            Toast.makeText(this, "Please enter a valid, non-negative number", Toast.LENGTH_SHORT).show()
            return
        }

        hoursWorkedData.add(Pair(selectedDate!!, hoursWorked))
        Toast.makeText(this, "Hours saved for $selectedDate: $hoursWorked", Toast.LENGTH_SHORT).show()

        updateChart()
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    calendar.apply { set(year, month, dayOfMonth) }.time
                )
                tvSelectedDate.text = "Selected Date: $selectedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setupBarChart() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
        }
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
    }

    private fun updateChart() {
        // Group by date and sum hours worked for each date
        val aggregatedData = hoursWorkedData
            .groupBy { it.first }
            .map { (date, entries) ->
                date to entries.sumOf { it.second.toDouble() }.toFloat()
            }
            .sortedBy { it.first }

        // Convert aggregated data into bar entries
        val entries = aggregatedData.mapIndexed { index, data ->
            BarEntry(index.toFloat(), data.second)
        }

        // Create dataset and style it
        val barDataSet = BarDataSet(entries, "Hours Worked").apply {
            color = ContextCompat.getColor(this@AnalyticsActivity, R.color.teal_700)
            valueTextColor = ContextCompat.getColor(this@AnalyticsActivity, R.color.black)
        }

        // Bind data to chart
        barChart.data = BarData(barDataSet)

        // Customize X-axis to show dates
        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(aggregatedData.map { it.first })

        // Refresh chart
        barChart.invalidate()
    }
}
