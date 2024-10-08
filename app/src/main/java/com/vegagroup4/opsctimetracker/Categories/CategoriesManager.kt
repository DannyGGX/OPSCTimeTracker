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

    var categories = mutableListOf<CategoryData>()

    public fun addCategory(category: CategoryData)
    {
        categories.add(category)
    }

    public fun getCategory(name: String): CategoryData?
    {
        for (category in categories)
        {
            if (category.name == name)
            {
                return category
            }
        }
        return null
    }
}