package data

import java.util.Date

data class ProjectViewModel(
    var name: String,
    var description: String,
    var startDate: Date?,
    var endDate: Date?,
    var minimumDailyHours: Int,
    var maximumDailyHours: Int,
    var tasks: MutableList<TaskViewModel>?) {
    constructor() : this("", "", null, null, -1, -1, null)
}
