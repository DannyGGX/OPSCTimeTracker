package com.vegagroup4.opsctimetracker.Entries

import com.vegagroup4.opsctimetracker.Categories.Category
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

data class Entry
(
    var title: String? = null,
    var description: String? = null,
    var category: Category,
    var project: Project,
    var client: Client,
    var minTimeGoal: Duration,
    var maxTimeGoal: Duration,
    var startTime: DateTimeFormatter,
    var endTime: DateTimeFormatter
)
