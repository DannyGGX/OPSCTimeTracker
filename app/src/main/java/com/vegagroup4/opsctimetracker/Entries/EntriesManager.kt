package com.vegagroup4.opsctimetracker.Entries


class EntriesManager private constructor()
{

    companion object
    {
        @Volatile private var instance: EntriesManager? = null
        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: EntriesManager().also { instance = it }
            }
    }

    var entries = mutableListOf<EntryData>()

    public fun addEntry(category: EntryData)
    {
        entries.add(category)
    }
}