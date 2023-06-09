package data

import java.io.Serializable
import java.util.Date

data class TaskViewModel(
    val name: String,
    val description: String,
    val startTime: Date?,
    val numberOfHours: Int,
    val photoUrl: String): Serializable {
    constructor() : this("", "", null, -1, "")
}

