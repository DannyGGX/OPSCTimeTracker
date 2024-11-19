package com.vegagroup4.opsctimetracker.User

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import com.vegagroup4.opsctimetracker.R
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var btnSaveHours: Button
    private lateinit var btnSelectDateRange: Button
    private lateinit var etHoursWorked: EditText
    private lateinit var hoursWorkedData: MutableList<Pair<String, Float>>  // Store hours worked data
    private lateinit var selectedDate: String // Store the selected date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        // Initialize the views using their IDs from XML
        lineChart = findViewById(R.id.lineChart)
        btnSaveHours = findViewById(R.id.btnSaveHours)  // Save Hours button
        btnSelectDateRange = findViewById(R.id.btnSelectDateRange)  // Date Range selection button
        etHoursWorked = findViewById(R.id.etHoursWorked)  // EditText for entering hours worked
        hoursWorkedData = mutableListOf()

        // Set up the chart
        setupLineChart()

        // Handle Save Hours button click
        btnSaveHours.setOnClickListener {
            saveHours()
        }

        // Handle Date Range button click
        btnSelectDateRange.setOnClickListener {
            selectDate()
        }
    }

    private fun saveHours() {
        // Validate if the date and hours are selected correctly
        if (::selectedDate.isInitialized) {
            val hoursWorked = etHoursWorked.text.toString().toFloatOrNull()
            if (hoursWorked != null) {
                // Save the hours worked for the selected date
                hoursWorkedData.add(Pair(selectedDate, hoursWorked))
                Toast.makeText(this, "Hours saved for $selectedDate: $hoursWorked", Toast.LENGTH_SHORT).show()

                // Optionally: Save the data to SharedPreferences or any other storage
                saveDataToDatabase(selectedDate, hoursWorked)

                // Update chart after saving hours
                updateChart()

            } else {
                Toast.makeText(this, "Please enter a valid number of hours", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select a valid date first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectDate() {
        // Show DatePicker and store the selected date
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Format the selected date as "yyyy-MM-dd"
                selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.apply {
                        set(year, month, dayOfMonth)
                    }.time)
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveDataToDatabase(date: String, hours: Float) {
        // Example of saving data to SharedPreferences (you can replace this with a database or Firebase)
        val sharedPreferences = getSharedPreferences("hours_data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat(date, hours)
        editor.apply()
    }

    private fun setupLineChart() {
        // Initialize chart with some dummy data
        val entries = mutableListOf<Entry>()
        entries.add(Entry(1f, 5f))
        entries.add(Entry(2f, 7f))
        entries.add(Entry(3f, 6f))
        entries.add(Entry(4f, 8f))
        entries.add(Entry(5f, 4f))

        val lineDataSet = LineDataSet(entries, "Hours Worked")
        lineDataSet.color = ContextCompat.getColor(this, R.color.teal_700)
        lineDataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        lineDataSet.lineWidth = 2f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f

        val leftAxis: YAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.isEnabled = false

        val minGoalLine = LimitLine(4f, "Min Goal").apply {
            lineColor = ContextCompat.getColor(this@AnalyticsActivity, R.color.purple_700)
            lineWidth = 1.5f
            textSize = 12f
        }

        val maxGoalLine = LimitLine(8f, "Max Goal").apply {
            lineColor = ContextCompat.getColor(this@AnalyticsActivity, R.color.red)
            lineWidth = 1.5f
            textSize = 12f
        }

        leftAxis.addLimitLine(minGoalLine)
        leftAxis.addLimitLine(maxGoalLine)

        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.invalidate()
    }

    private fun updateChart() {
        // Update the line chart with new data after hours are saved
        val entries = hoursWorkedData.mapIndexed { index, pair ->
            Entry(index.toFloat(), pair.second)
        }

        val lineDataSet = LineDataSet(entries, "Hours Worked")
        lineDataSet.color = ContextCompat.getColor(this, R.color.teal_700)
        lineDataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        lineDataSet.lineWidth = 2f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineChart.invalidate()  // Refresh the chart
    }
}
