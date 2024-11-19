package com.vegagroup4.opsctimetracker.User

data class UserData(
    val id: String? = null,
    val username: String? = null,
    val password: String? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val dailyLoginStreak: Int = 0,
    val tasksCompleted: Int = 0,
    val entriesCreated: Int = 0,

)
