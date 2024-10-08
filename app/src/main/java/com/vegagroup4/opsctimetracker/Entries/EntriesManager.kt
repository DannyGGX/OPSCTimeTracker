package com.vegagroup4.opsctimetracker.Entries

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
        databaseRef = database.reference.child("users")


    }

    var entries = mutableListOf<EntryData>()

    public fun addEntry(entry: EntryData)
    {
        val user: UserData? = UserManager.get()

        entries.add(entry)
        databaseRef.child(user!!.id!!).child("entries").push().setValue(entry)

    }
}