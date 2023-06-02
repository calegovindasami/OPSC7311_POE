package Data

import java.util.Date

data class ProjectViewModel(val name: String, val description: String, val startDate: Date, val endDate: Date, val minimumDailyHours: Int, val maximumDailyHours: Int, var tasks: MutableList<Task>)
