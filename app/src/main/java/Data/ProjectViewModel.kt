package Data

import com.google.android.gms.tasks.Task
import java.util.Date
import java.util.logging.SimpleFormatter

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
