package Data


import com.google.type.DateTime
import java.net.URL
import java.time.LocalDateTime
import java.util.Date

data class TaskViewModel(val name: String, val description: String, val startTime: Date?, val numberOfHours: Int, val photoUrl: String) {
    constructor() : this("", "", null, -1, "")
}
