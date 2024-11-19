package com.vegagroup4.opsctimetracker.Entries

import com.vegagroup4.opsctimetracker.Categories.CategoryData
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

data class EntryData
(
    var title: String,
    var description: String? = null,
    var category: CategoryData,
    var project: Project = Project(name = "None"),
    var client: Client = Client(name = "None"),
    var minTimeGoal: Int = 0,
    var maxTimeGoal: Int = 0,
    var startTime: DateTimeData,
    var endTime: DateTimeData,
    var isCompleted: Boolean = false,

)
