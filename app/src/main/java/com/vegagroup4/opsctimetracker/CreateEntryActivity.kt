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
        categorySpinner = findViewById(R.id.spinner_Category)
        newCategoryInput = findViewById(R.id.pt_NewCategoryInput)
        createEntryButton = findViewById(R.id.btn_CreateEntry)
        homeButton = findViewById(R.id.btn_Home)
        addCategoryButton = findViewById(R.id.btn_AddCategory)

        // Initialize categories
        categories.add("Work")
        categories.add("Personal")
        categories.add("Other")

        // Set up the spinner adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        createEntryButton.setOnClickListener {
            // Handle entry creation logic here
            // Get title, description, and selected category
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            val selectedCategory = categorySpinner.selectedItem.toString()

            // Add your logic to save the entry here

            // Optionally clear inputs after creation
            titleInput.text.clear()
            descriptionInput.text.clear()
        }

        homeButton.setOnClickListener {
            // Navigate back to MainMenuActivity
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

    // Function to add a new category
    private fun addNewCategory() {
        val newCategory = newCategoryInput.text.toString()
        if (newCategory.isNotEmpty() && !categories.contains(newCategory)) {
            categories.add(newCategory)
            (categorySpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            newCategoryInput.text.clear() // Clear the input after adding
        } else {
            // Optionally show a message that the category already exists
        }
    }
}
