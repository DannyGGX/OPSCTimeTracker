package com.vegagroup4.opsctimetracker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class CreateEntryActivity : AppCompatActivity() {

    private lateinit var btnSelectDate: Button
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnSubmit: Button
    private lateinit var btnAddCategory: Button
    private lateinit var tvDate: TextView
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etProject: EditText
    private lateinit var etClient: EditText
    private lateinit var etMinTime: EditText
    private lateinit var etMaxTime: EditText
    private lateinit var etNewCategory: EditText
    private lateinit var spinnerCategory: Spinner

    private var selectedDate: String = ""
    private var selectedStartTime: String = ""
    private var selectedEndTime: String = ""

    // Store categories in a mutable list
    private val categoryList = mutableListOf<String>()
    private lateinit var categoryAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_entry) // Ensure this matches your layout file name

        // Initialize Views
        btnSelectDate = findViewById(R.id.btn_SelectDate)
        btnStartTime = findViewById(R.id.btn_StartTime)
        btnEndTime = findViewById(R.id.btn_EndTime)
        btnSubmit = findViewById(R.id.btn_Submit)
        btnAddCategory = findViewById(R.id.btn_AddCategory)
        tvDate = findViewById(R.id.tv_Date)
        tvStartTime = findViewById(R.id.tv_StartTime)
        tvEndTime = findViewById(R.id.tv_EndTime)
        etTitle = findViewById(R.id.pt_TitleInput)
        etDescription = findViewById(R.id.pt_DescriptionInput)
        etProject = findViewById(R.id.pt_Project)
        etClient = findViewById(R.id.pt_Client)
        etMinTime = findViewById(R.id.pt_MinTimeInput)
        etMaxTime = findViewById(R.id.pt_MaxTimeInput)
        etNewCategory = findViewById(R.id.pt_NewCategoryInput)
        spinnerCategory = findViewById(R.id.spinner_Category)

        // Initialize the category adapter
        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        // Set click listeners
        btnSelectDate.setOnClickListener { showDatePickerDialog() }
        btnStartTime.setOnClickListener { showTimePickerDialog(true) }
        btnEndTime.setOnClickListener { showTimePickerDialog(false) }
        btnSubmit.setOnClickListener { handleSubmit() }

        // Set listener for the Add Category button
        btnAddCategory.setOnClickListener { addCategory() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            tvDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            if (isStartTime) {
                selectedStartTime = time
                tvStartTime.text = selectedStartTime
            } else {
                selectedEndTime = time
                tvEndTime.text = selectedEndTime
            }
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun handleSubmit() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val projectName = etProject.text.toString()
        val clientName = etClient.text.toString()
        val minTime = etMinTime.text.toString()
        val maxTime = etMaxTime.text.toString()
        val selectedCategory = spinnerCategory.selectedItem.toString()

        // Handle submission logic here (e.g., save the entry to the database or send it to another activity)
        // For demonstration, just show a toast or log the values
        Toast.makeText(this, "Entry Submitted!", Toast.LENGTH_SHORT).show()
        // Log.d("CreateEntryActivity", "Title: $title, Date: $selectedDate, Start Time: $selectedStartTime, End Time: $selectedEndTime")
    }

    private fun addCategory() {
        val newCategory = etNewCategory.text.toString()

        // Add the new category if it's not empty and not already in the list
        if (newCategory.isNotEmpty() && !categoryList.contains(newCategory)) {
            categoryList.add(newCategory)
            categoryAdapter.notifyDataSetChanged() // Notify the adapter to update the spinner
            etNewCategory.text.clear() // Clear the input field
            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Category is empty or already exists", Toast.LENGTH_SHORT).show()
        }
    }
}
