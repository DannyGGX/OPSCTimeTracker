package com.vegagroup4.opsctimetracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vegagroup4.opsctimetracker.User.AnalyticsActivity
import com.vegagroup4.opsctimetracker.User.UserManager
import com.vegagroup4.opsctimetracker.databinding.ActivityMainMenuBinding
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
//import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vegagroup4.opsctimetracker.User.UserData

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding


    // New XP and Level System variables
    private lateinit var xpTextView: TextView
    private lateinit var levelTextView: TextView
    private lateinit var xpProgressBar: ProgressBar

    //private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private var currentXP: Int = 0
    private var currentLevel: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using View Binding
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize XP and Level views
        xpTextView = findViewById(R.id.xp_text_view)
        levelTextView = findViewById(R.id.level_text_view)
        xpProgressBar = findViewById(R.id.xp_progress_bar)

        // Initialize Firebase
        //auth = FirebaseAuth.getInstance()
        //val userId = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(UserManager.getUserId()!!)

        // Fetch initial user data, including XP and level
        fetchUserData()

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
        */
        binding.btnAnalytics.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity ::class.java)
            startActivity(intent)
        }

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

    private fun fetchUserData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Fetch current XP and level, preserving other data fetch logic
                val userData = snapshot.getValue(UserData::class.java)
                if (userData != null) {
                    currentXP = userData.xp
                    currentLevel = userData.level}

                // Update the UI for XP and Level
                updateXPUI()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainMenuActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Method to award XP and handle level up
    private fun awardXP(xpEarned: Int) {
        currentXP += xpEarned
        var xpNeededForNextLevel = 50 * currentLevel

        // Check if the user should level up
        while (currentXP >= xpNeededForNextLevel) {
            currentLevel++
            currentXP -= xpNeededForNextLevel
            xpNeededForNextLevel = 50 * currentLevel

            // Notify user of level up
            Toast.makeText(this, "Level Up! You are now Level $currentLevel", Toast.LENGTH_SHORT).show()
        }

        // Update Firebase with new XP and level values
        databaseReference.child("xp").setValue(currentXP)
        databaseReference.child("level").setValue(currentLevel)

        // Update the UI
        updateXPUI()
    }

    private fun updateXPUI() {
        levelTextView.text = "Level: $currentLevel"
        val xpNeededForNextLevel = 50 * currentLevel
        xpProgressBar.max = xpNeededForNextLevel
        xpProgressBar.progress = currentXP
        xpTextView.text = "XP: $currentXP/$xpNeededForNextLevel"
    }

    // Example XP award methods for different actions
    fun onTaskCompleted() {
        awardXP(10)
    }

    fun onDailyLogin() {
        awardXP(10)
    }

    fun onWeeklyStreak() {
        awardXP(70)
    }

    fun onMultipleTasksCompleted() {
        awardXP(100)
    }

    fun onEntriesCreated(entries: Int) {
        if (entries == 3) {
            awardXP(30)
        } else if (entries == 10) {
            awardXP(100)
        }
    }
}
