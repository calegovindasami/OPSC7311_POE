package Services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.HoursViewModel
import data.ProjectViewModel
import data.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HoursService {

    private var db: FirebaseFirestore = Firebase.firestore



    private fun getHours(projects:MutableList<ProjectViewModel>,startDate:Date,endDate:Date) : MutableList<HoursViewModel>
    {
        var totalHours = mutableListOf<HoursViewModel>()

        for (proj in projects)
        {
            var hours = 0
            if (proj.startDate!!.compareTo(startDate) >= 1 && proj.endDate!!.compareTo(endDate) <= 1 )
            {
                var tasks = proj.tasks

                tasks!!.forEach {
                        t ->
                    hours += t.numberOfHours
                }

                totalHours.add(HoursViewModel(proj.name,hours, proj.tasks!!.size))

            }
        }

        return totalHours
    }

    //Method will take in the project that was selected as the parameter
    fun getMonthlyTasks(project: ProjectViewModel): MutableList<TaskViewModel>
    {
        var tasks = project.tasks
        var monthlyTasks : MutableList<TaskViewModel> = mutableListOf()

        val formatter = DateTimeFormatter.ofPattern("EEE MMM d H:mm:ss 'GMT'xxx yyyy", Locale.ENGLISH)
      //  val localDate = LocalDate.parse(task.date, formatter)
        val current = LocalDate.now()

        tasks!!.forEach{
            t ->


            var date = LocalDate.parse(t.startTime.toString(),formatter)
            var month = date.month

            if (month == current.month){
                monthlyTasks.add(t)
            }

        }

        return monthlyTasks

    }


 /*   fun getTasksByWeekOfMonth(tasks: List<TaskViewModel>): Map<Int, List<TaskViewModel>> {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM d H:mm:ss 'GMT'xxx yyyy", Locale.ENGLISH)
        val currentMonth = LocalDate.now().monthValue

        // Group tasks by week of the month
        val tasksByWeekOfMonth = tasks.groupBy { task ->
            val localDate = LocalDate.parse(task.startTime.toString(), formatter)
            val weekOfMonth = localDate.get(java.time.temporal.IsoFields.WEEK_OF_MONTH)
            weekOfMonth
        }

        // Filter tasks for the current month
        val filteredTasksByWeekOfMonth = tasksByWeekOfMonth.filterKeys { weekOfMonth ->
            val firstTask = tasksByWeekOfMonth[weekOfMonth]?.firstOrNull()
            val taskMonth = LocalDate.parse(firstTask?.startTime.toString(), formatter).monthValue
            taskMonth == currentMonth
        }

        return filteredTasksByWeekOfMonth
    }
    */
    fun calcAverage(tasks : MutableList<TaskViewModel>) : MutableList<Int>
    {
        //Gets total number of days from current month
        val c = Calendar.getInstance()
        val monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH)

        val formatter = DateTimeFormatter.ofPattern("EEE MMM d H:mm:ss 'GMT'xxx yyyy", Locale.ENGLISH)

        val weekOne: MutableList<TaskViewModel> = mutableListOf()
        val weekTwo: MutableList<TaskViewModel> = mutableListOf()
        val weekThree: MutableList<TaskViewModel> = mutableListOf()
        val weekFour: MutableList<TaskViewModel> = mutableListOf()


        var stats = mutableListOf<Int>()

        //Running through all the tasks from the project
        tasks!!.forEach{
            t ->
            //Get the day of each task
            var date = LocalDate.parse(t.startTime.toString(),formatter)
            var day = date.dayOfMonth

            //Check what week the task falls under
            if (day <= 7)
            {
                weekOne.add(t)
            }
            else
            {
                if (day>7 && day<=14)
                {
                    weekTwo.add(t)
                }
                else
                {
                    if (day > 14 && day <= 21)
                    {
                        weekThree.add(t)
                    }
                    else
                    {
                        weekFour.add(t)
                    }
                }
            }//End All Ifs

        }//End For Each

        //Store the weekly tasks in a list
        var array = mutableListOf(weekOne,weekTwo,weekThree,weekFour)

        //loop through the 2D list and get the hours completed for each task
        for (week in array)
        {
            var hours = 0
            for (task in week)
            {
                hours += task.numberOfHours
            }
            stats.add(hours)
        }


        return stats

    }

}