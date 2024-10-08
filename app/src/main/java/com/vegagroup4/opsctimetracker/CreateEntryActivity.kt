package com.vegagroup4.opsctimetracker

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class CreateEntryActivity : AppCompatActivity() {

    private lateinit var btnSelectDate: Button
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnSubmit: Button
    private lateinit var btnAddCategory: Button
    private lateinit var btnHome: Button  // Home button
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

    private lateinit var btnTakePhoto: Button
    private lateinit var imgPhoto: ImageView

    private var selectedDate: String = ""
    private var selectedStartTime: String = ""
    private var selectedEndTime: String = ""

    private val CAMERA_REQUEST_CODE = 100
    private val CAMERA_PERMISSION_CODE = 101

    // Store categories in a mutable list
    private val categoryList = mutableListOf<String>()
    private lateinit var categoryAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_entry)

        // Initialize Views
        btnSelectDate = findViewById(R.id.btn_SelectDate)
        btnStartTime = findViewById(R.id.btn_StartTime)
        btnEndTime = findViewById(R.id.btn_EndTime)
        btnSubmit = findViewById(R.id.btn_Submit)
        btnAddCategory = findViewById(R.id.btn_AddCategory)
        btnHome = findViewById(R.id.btn_Home)  // Initialize Home button
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

        btnTakePhoto = findViewById(R.id.btn_TakePhoto)
        imgPhoto = findViewById(R.id.img_Photo)

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

        // Handle Home button click
        btnHome.setOnClickListener { navigateToHome() }

        // Set camera button click listener
        btnTakePhoto.setOnClickListener { checkCameraPermission() }
    }

    // Camera permission handling
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            openCamera()
        } else {
            // Request the camera permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Open the camera to take a photo
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    // Handle the result of the camera activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imgPhoto.setImageBitmap(photo)  // Display the photo in the ImageView
        }
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

        Toast.makeText(this, "Entry Submitted!", Toast.LENGTH_SHORT).show()
    }

    private fun addCategory() {
        val newCategory = etNewCategory.text.toString()

        if (newCategory.isNotEmpty() && !categoryList.contains(newCategory)) {
            categoryList.add(newCategory)
            categoryAdapter.notifyDataSetChanged()
            etNewCategory.text.clear()
            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Category is empty or already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome() {
        // Navigate to home activity (replace MainActivity with the appropriate home screen activity)
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity if needed
    }
}
