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
        var monthlyTasks : MutableList<TaskViewModel> = mutableListOf(TaskViewModel())

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


    fun calcAverage(tasks : MutableList<TaskViewModel>)
    {

    }

}