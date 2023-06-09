package data

import java.io.Serializable
import java.util.Date

data class TaskViewModel(
    var name: String,
    var description: String,
    var startTime: Date?,
    var numberOfHours: Int,
    var photoUrl: String): Serializable {
    constructor() : this("", "", null, -1, "")
}

