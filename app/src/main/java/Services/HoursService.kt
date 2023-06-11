package Services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.HoursViewModel
import data.ProjectViewModel
import java.util.*

class HoursService {

    private var db: FirebaseFirestore = Firebase.firestore

    private fun filterProjects(start: Date, end: Date, projectList: MutableList<ProjectViewModel>): MutableList<ProjectViewModel> {
        val filteredList: MutableList<ProjectViewModel> = mutableListOf()

        for (proj in projectList) {
            if (proj.startDate!!.compareTo(start) >= 1 && proj.endDate!!.compareTo(end) < 1) {
                filteredList.add(proj)
            }
        }

        return filteredList

    }

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

}