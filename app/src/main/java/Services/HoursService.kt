package Services

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.HoursViewModel
import data.ProjectViewModel
import data.TaskViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class HoursService {

    //Gets all of the tasks for every project that a user has
    @SuppressLint("SuspiciousIndentation")
    fun getTasks(projects: MutableList<ProjectViewModel>): MutableList<TaskViewModel>
    {
        var tasks = mutableListOf<TaskViewModel>()

        projects.forEach(){
                proj ->
            var projTasks = proj.tasks



                projTasks!!.forEach {
                        t->
                    tasks.add(t)
                }
            }


        return tasks
    }

    //Gets the average hours a user should be working every day
    fun getAverageHours(projects: MutableList<ProjectViewModel>, days: Int): Double {
        var maxHours = 0
        var minHours = 0

        projects.forEach() { p ->
            maxHours += p.maximumDailyHours
            minHours += p.minimumDailyHours
        }

        return ((maxHours.toDouble()- minHours.toDouble()) / days)
    }

    //Checks if a user is within their daily goals
    fun checkUserHours(tasks: MutableList<TaskViewModel>):Int
    {
        var userHours = 0

        tasks.forEach(){
            t->
            userHours += t.numberOfHours
        }

        return userHours
    }


    //Gets the data that will be displayed in y-axis of graph
    fun getGraphData(tasks : MutableList<TaskViewModel>,start: Date, end: Date): MutableList<Int>{

        var current = start
        var hours = mutableListOf<Int>()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        while(current <= end)
        {

            var total = 0
            tasks.forEach(){
                    t->


                var taskCal = Calendar.getInstance()
                taskCal.time = t.startTime!!
                val taskDateString = taskCal.get(Calendar.DAY_OF_MONTH).toString() + "-" + (taskCal.get(Calendar.MONTH) + 1) + "-" + taskCal.get(Calendar.YEAR)
                val taskDate = dateFormat.parse(taskDateString)

                val currentCal = Calendar.getInstance()
                currentCal.time = current
                val currentDateString = currentCal.get(Calendar.DAY_OF_MONTH).toString() + "-" + (currentCal.get(Calendar.MONTH) + 1) + "-" + currentCal.get(Calendar.YEAR)
                val currentDate = dateFormat.parse(currentDateString)

                if (taskDate == currentDate)
                {
                    total += t.numberOfHours
                }
            }

            if (total > 0) {
                hours.add(total)
            }

            // Increment current by 1 day
            calendar.time = current
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            current = calendar.time
            //Increment current here:

        }

        return hours
    }



}