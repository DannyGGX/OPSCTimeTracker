package com.vegagroup4.opsctimetracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vegagroup4.opsctimetracker.Categories.CategoryData
import com.vegagroup4.opsctimetracker.Entries.Client
import com.vegagroup4.opsctimetracker.Entries.DateTimeData
import com.vegagroup4.opsctimetracker.Entries.EntriesListAdapter
import com.vegagroup4.opsctimetracker.Entries.EntriesManager
import com.vegagroup4.opsctimetracker.Entries.EntryData
import com.vegagroup4.opsctimetracker.Entries.Project
import com.vegagroup4.opsctimetracker.User.UserManager
import com.vegagroup4.opsctimetracker.databinding.ActivityListEntriesBinding


class EntriesListActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityListEntriesBinding
    private lateinit var databaseRef: DatabaseReference
    private var entriesList = mutableListOf<EntryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }
        binding.btnCreateNewEntry.setOnClickListener {
            val intent = Intent(this, CreateEntryActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.recyclerView.adapter = EntriesListAdapter(entriesList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true) // probably not needed

        databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(UserManager.getUserId()!!).child("entries")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                entriesList.clear() // clear the list to avoid duplication
                for(entrySnapshot in dataSnapshot.children) {
                    val entry = readEntryFromDatabase(entrySnapshot)
                    entriesList.add(entry)
                }
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EntriesListActivity, "Failed to read from database", Toast.LENGTH_SHORT).show()
            }
        })


    }
    
    private fun readEntryFromDatabase(entrySnapshot: DataSnapshot): EntryData
    {
        val entry = EntryData(
            entrySnapshot.child("title").getValue(String::class.java) ?: "",
            "",
            CategoryData(
                entrySnapshot.child("category").child("name").getValue(String::class.java) ?: "",
                entrySnapshot.child("category").child("color").getValue(Int::class.java) ?: Color.RED
            ),
            Project(entrySnapshot.child("project").child("name").getValue(String::class.java) ?: ""),
            Client(""),
            0,
            0,
            getDateTimeFromDatabase(entrySnapshot.child("startTime")),
            getDateTimeFromDatabase(entrySnapshot.child("endTime"))
        )
        return entry
    }
    private fun getDateTimeFromDatabase(dateTimeSnapshot: DataSnapshot): DateTimeData
    {
        val day = dateTimeSnapshot.child("day").getValue(Int::class.java) ?: 0
        val hour = dateTimeSnapshot.child("hour").getValue(Int::class.java) ?: 0
        val minute = dateTimeSnapshot.child("minute").getValue(Int::class.java) ?: 0
        val month = dateTimeSnapshot.child("month").getValue(Int::class.java) ?: 0
        val year = dateTimeSnapshot.child("year").getValue(Int::class.java) ?: 0
        return DateTimeData(year, month, day, hour, minute)
    }



}