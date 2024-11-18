package com.vegagroup4.opsctimetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vegagroup4.opsctimetracker.databinding.ActivityListEntriesBinding


class EntriesListActivity : AppCompatActivity() {

    private lateinit var btnHome: Button  // Home button

    private lateinit var binding: ActivityListEntriesBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inflate the layout using View Binding
        binding = ActivityListEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        btnHome = findViewById(R.id.btn_Home)  // Initialize Home button

        // Handle Home button click
        btnHome.setOnClickListener { navigateToHome() }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCreateNewEntry.setOnClickListener {
            val intent = Intent(this, CreateEntryActivity::class.java)
            startActivity(intent)
        }


    }

    private fun navigateToHome() {
        // Navigate to home activity (replace MainActivity with the appropriate home screen activity)
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity if needed
    }
}