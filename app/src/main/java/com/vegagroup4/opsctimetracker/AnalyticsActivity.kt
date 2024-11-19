package com.vegagroup4.opsctimetracker.User

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.vegagroup4.opsctimetracker.R
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
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
        lineChart = findViewById(R.id.lineChart)
        btnSaveHours = findViewById(R.id.btnSaveHours)
        btnSelectDateRange = findViewById(R.id.btnSelectDateRange)
        etHoursWorked = findViewById(R.id.etHoursWorked)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)

        setupLineChart()

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

    private fun setupLineChart() {
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
        }
        lineChart.axisLeft.apply {
            setDrawGridLines(true)
            axisMinimum = 0f
        }
        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
    }

    private fun updateChart() {
        // Group by date and sum hours worked for each date
        val aggregatedData = hoursWorkedData
            .groupBy { it.first }  // Group by date (String)
            .map { (date, entries) ->
                date to entries.sumOf { it.second.toDouble() }.toFloat()  // Explicit type handling
            }
            .sortedBy { it.first }  // Sort by date

        // Convert aggregated data into chart entries
        val entries = aggregatedData.mapIndexed { index, data ->
            Entry(index.toFloat(), data.second)
        }

        // Create dataset and style it
        val lineDataSet = LineDataSet(entries, "Hours Worked").apply {
            color = ContextCompat.getColor(this@AnalyticsActivity, R.color.teal_700)
            valueTextColor = ContextCompat.getColor(this@AnalyticsActivity, R.color.black)
            lineWidth = 2f
            setCircleColor(ContextCompat.getColor(this@AnalyticsActivity, R.color.teal_700))
            circleRadius = 4f
            mode = LineDataSet.Mode.CUBIC_BEZIER  // Smooth line style (optional)
        }

        // Bind data to chart
        lineChart.data = LineData(lineDataSet)

        // Calculate Y-axis range dynamically
        val yValues = entries.map { it.y }
        val minY = yValues.minOrNull() ?: 0f
        val maxY = yValues.maxOrNull() ?: 0f
        val buffer = (maxY - minY) * 0.1f  // Add a 10% buffer for padding

        // Set Y-axis range
        val leftAxis: YAxis = lineChart.axisLeft
        leftAxis.axisMinimum = minY - buffer
        leftAxis.axisMaximum = maxY + buffer

        // Disable right Y-axis (optional)
        lineChart.axisRight.isEnabled = false

        // Customize X-axis to show dates
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f  // Ensure labels align with data points
        xAxis.valueFormatter = IndexAxisValueFormatter (aggregatedData.map { it.first })  // Use dates as labels

        // Refresh chart
        lineChart.invalidate()
    }



}
