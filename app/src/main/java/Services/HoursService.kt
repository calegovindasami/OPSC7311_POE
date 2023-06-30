package Services

import android.annotation.SuppressLint
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
            if (proj.startDate!!.compareTo(startDate) >1 && proj.endDate!!.compareTo(endDate) <= 1 )
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


    //Gets all of the tasks for every project that a user has
    @SuppressLint("SuspiciousIndentation")
    fun getTasks(projects: MutableList<ProjectViewModel>): MutableList<TaskViewModel>
    {
        var tasks = mutableListOf<TaskViewModel>()

        projects.forEach(){
                proj ->
                var projTasks = proj.tasks

                    if (projTasks != null) {
                        projTasks.forEach {
                                t->
                                tasks.add(t)
                        }
                    }
        }

        return tasks
    }

    //Gets total hours for every week in the month
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

    fun calcBarAverage(tasks : MutableList<TaskViewModel>,week: Int) : IntArray
    {
        //Gets total number of days from current month
        val c = Calendar.getInstance()
        val monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH)

        val formatter = DateTimeFormatter.ofPattern("EEE MMM d H:mm:ss 'GMT'xxx yyyy", Locale.ENGLISH)

        val weekOne: MutableList<TaskViewModel> = mutableListOf()
        val weekTwo: MutableList<TaskViewModel> = mutableListOf()
        val weekThree: MutableList<TaskViewModel> = mutableListOf()
        val weekFour: MutableList<TaskViewModel> = mutableListOf()


        var stats = IntArray(7)

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



        when (week) {
            1 -> {
                var hours = 0
                for (task in weekOne)
                {
                    var i = 1
                    var j = 0

                    var date = LocalDate.parse(task.startTime.toString(),formatter)
                    var day = date.dayOfMonth
                    while (i <= 1 && i<=7)
                    {
                        if (day == i)
                        {
                            stats[j]+=task.numberOfHours
                        }
                        i++
                        j++
                    }


                }

            }
            2 -> {
                for (task in weekTwo)
                {
                    var i = 8
                    var j = 0

                    var date = LocalDate.parse(task.startTime.toString(),formatter)
                    var day = date.dayOfMonth
                    while (i >= 8 && i<=14)
                    {
                        if (day == i)
                        {
                            stats[j]+=task.numberOfHours
                        }
                        i++
                        j++
                    }


                }
            }
            3 ->{
                for (task in weekThree)
                {
                    var i = 15
                    var j = 0

                    var date = LocalDate.parse(task.startTime.toString(),formatter)
                    var day = date.dayOfMonth
                    while (i >= 15 && i<=21)
                    {
                        if (day == i)
                        {
                            stats[j]+=task.numberOfHours
                        }
                        i++
                        j++
                    }


                }
            }
            else -> {
                for (task in weekFour)
                {
                    var i = 22
                    var j = 0

                    var date = LocalDate.parse(task.startTime.toString(),formatter)
                    var day = date.dayOfMonth
                    while (i >= 22 && i<=monthMaxDays)
                    {
                        if (day == i)
                        {
                            stats[j]+=task.numberOfHours
                        }
                        i++
                        j++
                    }


                }
            }
        }

        return stats

    }



}