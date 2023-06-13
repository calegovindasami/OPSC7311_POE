package data

data class HoursViewModel(
    var projectName:String,
    var totalHours:Int,
    var numberOfTasks:Int
)
{
    constructor(): this("",-1,-1)
}
