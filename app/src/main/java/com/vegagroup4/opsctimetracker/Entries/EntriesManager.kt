package com.vegagroup4.opsctimetracker.Entries

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vegagroup4.opsctimetracker.User.UserData
import com.vegagroup4.opsctimetracker.User.UserManager


class EntriesManager private constructor()
{
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    companion object
    {
        @Volatile private var instance: EntriesManager? = null
        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: EntriesManager().also { instance = it }
            }

    }

    init {
        database = FirebaseDatabase.getInstance()


    }

    var entries = mutableListOf<EntryData>()

    public fun addEntry(entry: EntryData, context: Context)
    {
        val user: UserData? = UserManager.get()


        entries.add(entry)
        databaseRef.child(user!!.id!!).child("entries").push().setValue(entry)
            .addOnSuccessListener {
                Toast.makeText(context, "Entry added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add entry", Toast.LENGTH_SHORT).show()
            }
//        val entryRef = databaseRef.child(user.id!!).child("entries").child(entry.title)
//        entryRef.push().setValue(entry.description)
//
//        entryRef.push().setValue(entry.category.name)
//        entryRef.push().setValue(entry.category.color)
//
//        entryRef.push().setValue(entry.project.name)
//        entryRef.push().setValue(entry.client.name)
//
//        entryRef.push().setValue(entry.minTimeGoal)
//        entryRef.push().setValue(entry.maxTimeGoal)
//
//        entryRef.push().setValue(entry.startTime.day)
//        entryRef.push().setValue(entry.startTime.month)
//        entryRef.push().setValue(entry.startTime.year)
//        entryRef.push().setValue(entry.startTime.hour)
//        entryRef.push().setValue(entry.startTime.minute)
//
//        entryRef.push().setValue(entry.endTime.day)
//        entryRef.push().setValue(entry.endTime.month)
//        entryRef.push().setValue(entry.endTime.year)
//        entryRef.push().setValue(entry.endTime.hour)
//        entryRef.push().setValue(entry.endTime.minute)
    }

//    public fun readEntryFromDatabase(entryRef: DatabaseReference): EntryData
//    {
//        var entry: EntryData
//        entry.title = entryRef.
//    }



}