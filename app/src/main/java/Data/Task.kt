package Data

import com.google.type.DateTime
import java.net.URL

data class Task(val name: String, val description: String, val startTime: DateTime, val numberOfHours: Int, val photoUrl: URL)
