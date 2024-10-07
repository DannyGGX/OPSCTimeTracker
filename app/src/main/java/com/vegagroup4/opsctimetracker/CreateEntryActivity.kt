package com.vegagroup4.opsctimetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateEntryActivity : AppCompatActivity() {
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var newCategoryInput: EditText
    private lateinit var createEntryButton: Button
    private lateinit var homeButton: Button
    private lateinit var addCategoryButton: Button

    private val categories = mutableListOf("Work", "Personal", "Other") // Initial categories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_entry)

        // Initialize views
        titleInput = findViewById(R.id.pt_TitleInput)
        descriptionInput = findViewById(R.id.pt_DescriptionInput)
        categorySpinner = findViewById(R.id.spinner_Category)
        newCategoryInput = findViewById(R.id.pt_NewCategoryInput)
        createEntryButton = findViewById(R.id.btn_CreateEntry)
        homeButton = findViewById(R.id.btn_Home)
        addCategoryButton = findViewById(R.id.btn_AddCategory)

        // Set up the spinner adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Handle button clicks
        createEntryButton.setOnClickListener {
            createEntry() // Call to create entry function
        }

        homeButton.setOnClickListener {
            finish() // Close current activity to return to the previous one
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

    // Function to create an entry
    private fun createEntry() {
        val title = titleInput.text.toString()
        val description = descriptionInput.text.toString()
        val selectedCategory = categorySpinner.selectedItem.toString()

        // Validate inputs
        if (title.isBlank()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }
        if (description.isBlank()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }

        // Add logic to save the entry here

        // Clear inputs after creation
        titleInput.text.clear()
        descriptionInput.text.clear()
        Toast.makeText(this, "Entry created!", Toast.LENGTH_SHORT).show()
    }

    // Function to add a new category
    private fun addNewCategory() {
        val newCategory = newCategoryInput.text.toString()
        if (newCategory.isNotEmpty() && !categories.contains(newCategory)) {
            categories.add(newCategory)
            (categorySpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            newCategoryInput.text.clear() // Clear the input after adding
            Toast.makeText(this, "Category added!", Toast.LENGTH_SHORT).show()
        } else if (categories.contains(newCategory)) {
            Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Category cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}
