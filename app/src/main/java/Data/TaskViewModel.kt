package Data


import com.google.type.DateTime
import java.net.URL
import java.time.LocalDateTime
import java.util.Date

data class TaskViewModel(var name: String, var description: String, var startTime: Date?, var numberOfHours: Int, var photoUrl: String) {
    constructor() : this("", "", null, -1, "")
}
