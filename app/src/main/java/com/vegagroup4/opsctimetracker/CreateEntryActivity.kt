package com.vegagroup4.opsctimetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateEntryActivity : AppCompatActivity() {
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var projectInput: EditText
    private lateinit var clientInput: EditText
    private lateinit var minTimeInput: EditText
    private lateinit var maxTimeInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var newCategoryInput: EditText
    private lateinit var createEntryButton: Button
    private lateinit var homeButton: Button
    private lateinit var addCategoryButton: Button

    private val categories = mutableListOf<String>() // List to hold categories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_entry)

        titleInput = findViewById(R.id.pt_TitleInput)
        descriptionInput = findViewById(R.id.pt_DescriptionInput)
        projectInput = findViewById(R.id.pt_Project)
        clientInput = findViewById(R.id.pt_Client)
        minTimeInput = findViewById(R.id.pt_MinTimeInput)
        maxTimeInput = findViewById(R.id.pt_MaxTimeInput)
        categorySpinner = findViewById(R.id.spinner_Category)
        newCategoryInput = findViewById(R.id.pt_NewCategoryInput)
        createEntryButton = findViewById(R.id.btn_CreateEntry)
        homeButton = findViewById(R.id.btn_Home)
        addCategoryButton = findViewById(R.id.btn_AddCategory)

        // Initialize default categories
        categories.add("Work")
        categories.add("Personal")
        categories.add("Other")

        // Set up the spinner adapter for categories
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        createEntryButton.setOnClickListener {
            // Handle entry creation logic here
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            val project = projectInput.text.toString()
            val client = clientInput.text.toString()
            val minTime = minTimeInput.text.toString().toIntOrNull() ?: 0
            val maxTime = maxTimeInput.text.toString().toIntOrNull() ?: 0
            val selectedCategory = categorySpinner.selectedItem.toString()

            // Ensure maxTime is greater than minTime
            if (maxTime < minTime) {
                maxTimeInput.error = "Max time should be greater than min time"
                return@setOnClickListener
            }

            // Add logic to save or process the entry
            // Use the title, description, project, client, minTime, maxTime, and selectedCategory

            // Optionally clear the input fields after entry creation
            titleInput.text.clear()
            descriptionInput.text.clear()
            projectInput.text.clear()
            clientInput.text.clear()
            minTimeInput.text.clear()
            maxTimeInput.text.clear()
        }

        homeButton.setOnClickListener {
            // Navigate back to MainMenuActivity
            finish() // Close the current activity and return to the previous one
        }

        addCategoryButton.setOnClickListener {
            addNewCategory() // Call the function to add a new category
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to add a new category
    private fun addNewCategory() {
        val newCategory = newCategoryInput.text.toString()
        if (newCategory.isNotEmpty() && !categories.contains(newCategory)) {
            categories.add(newCategory)
            (categorySpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            newCategoryInput.text.clear() // Clear the input field after adding the category
        } else {
            // Optionally show a message if the category already exists or is empty
        }
    }
}
