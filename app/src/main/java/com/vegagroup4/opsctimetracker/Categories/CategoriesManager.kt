package com.vegagroup4.opsctimetracker.Categories

class CategoriesManager private constructor()
{
    companion object
    {
        @Volatile private var instance: CategoriesManager? = null
        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: CategoriesManager().also { instance = it }
            }
    }


}