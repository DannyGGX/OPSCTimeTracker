package com.vegagroup4.opsctimetracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vegagroup4.opsctimetracker.User.UserManager
import com.vegagroup4.opsctimetracker.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using View Binding
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button click listeners

        /*binding.btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnCalender.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        */
        binding.btnListOfEntries.setOnClickListener {
            val intent = Intent(this, EntriesListActivity::class.java)
            startActivity(intent)
        }


        binding.btnEntries.setOnClickListener {
            val intent = Intent(this, CreateEntryActivity::class.java)
            startActivity(intent)
        }
        /*
        binding.btnTeams.setOnClickListener {
            val intent = Intent(this, TeamsActivity::class.java)
            startActivity(intent)
        }

        binding.btnReports.setOnClickListener {
            val intent = Intent(this, ReportsActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

         */

        // Add the logged in user name
        binding.txtWelcometext.text = "Welcome, ${UserManager.get()?.username}"
    }
}
