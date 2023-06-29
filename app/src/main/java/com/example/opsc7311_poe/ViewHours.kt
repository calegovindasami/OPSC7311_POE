package com.example.opsc7311_poe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import data.HoursViewAdapter
import data.HoursViewModel
import data.ProjectViewAdapter
import data.ProjectViewModel
import data.TaskViewAdapter
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewHours.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewHours : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var uid: String
    private var db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_hours, container, false)

        var projectList: MutableList<ProjectViewModel> = mutableListOf()
        var hours: MutableList<HoursViewModel> = mutableListOf()



        val args = arguments
        if (args != null && args.containsKey("startDate") || args!!.containsKey("endDate")) {
            val startDate = Date(args.getLong("startDate")) // Retrieve the date
            val endDate = Date(args.getLong("endDate"))
            uid = args.getString("uid")!!


            //Gets document snapshot of all user projects.
            val docRef = db.collection("users").document(uid).collection("projects")
            docRef.get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    var projects = it.result.documents

                    for (p in projects) {
                        projectList.add(p.toObject<ProjectViewModel>()!!)
                    }

                    //Filters projects by date.
                    var filteredProjects = filterProjects(startDate, endDate, projectList)

                    var hours = getHours(filteredProjects)
                    //Assigns adapter to recyclerview
                    val recyclerView = view.findViewById<RecyclerView>(R.id.project_hours_view)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    val adapter = HoursViewAdapter(hours)
                    recyclerView.adapter = adapter

                }
            }

        }

        val btnBack = view.findViewById<ImageButton>(R.id.btnHoursBack)
        btnBack.setOnClickListener() {
            //Switches view back to projects list
            val projectView = ViewProject.newInstance()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.flNavigation, projectView).commit()
        }
        return view
    }

    private fun filterProjects(start: Date, end: Date, projectList: MutableList<ProjectViewModel>): MutableList<ProjectViewModel> {
        val filteredList: MutableList<ProjectViewModel> = mutableListOf()

        for (proj in projectList) {
            if (proj.startDate!!.compareTo(start) > 1 || proj.startDate!!.compareTo(start) == 0 && proj.endDate!!.compareTo(end) < 1 || proj.endDate!!.compareTo(end) == 0) {
                filteredList.add(proj)
            }
        }

        return filteredList

    }

    //Calculates total hours for each project.
    private fun getHours(projects:MutableList<ProjectViewModel>) : MutableList<HoursViewModel>
    {
        var totalHours = mutableListOf<HoursViewModel>()

        for (proj in projects)
        {
            var hours = 0

                var tasks = proj.tasks

                tasks!!.forEach {
                        t ->
                    hours += t.numberOfHours
                }

                totalHours.add(HoursViewModel(proj.name,hours, proj.tasks!!.size))
        }

        return totalHours
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewHours.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewHours().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}