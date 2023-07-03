package ProjectForm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.opsc7311_poe.HomeFragment
import com.example.opsc7311_poe.R
import com.example.opsc7311_poe.ViewProject
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import data.ProjectViewModel
import data.TaskViewModel
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectForm.newInstance] factory method to
 * create an instance of this fragment.
 */

private var startDate: Date? = null
private var endDate: Date? = null



class ProjectForm : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var db: FirebaseFirestore = Firebase.firestore
    private lateinit var auth: FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_project_form, container, false)

        val btnDateRange = view.findViewById<Button>(R.id.btnProjectDateRange)

        btnDateRange.setOnClickListener() {
            createDatePicker(btnDateRange)
        }

        val btnBack = view.findViewById<ImageButton>(R.id.btnProjectBack)

        val projectList: MutableList<ProjectViewModel> = mutableListOf()
        val auth = Firebase.auth
        val uid = auth.uid!!

        val docRef =  db.collection("users").document(uid).collection("projects")
        docRef.get().addOnCompleteListener() {
            if (it.isSuccessful) {
                var projects = it.result.documents
                for (p in projects) {
                    var project = p.toObject<ProjectViewModel>()
                    projectList.add(project!!)
                }
                val fragment: Fragment

                if (projectList.size == 0) {
                    val homeFrag = HomeFragment()
                    fragment = homeFrag
                }
                else {
                    val viewProject = ViewProject()
                    fragment = viewProject
                }

                btnBack.setOnClickListener() {
                    requireActivity().supportFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        replace(R.id.flNavigation, fragment)
                        addToBackStack(null)
                    }
                }
            }
        }

        val btnSubmit = view.findViewById<Button>(R.id.btnProjectSubmit)



        btnSubmit.setOnClickListener {
            addProject(getFormData(view))
        }

        return view
    }

    //Retrieves form data from corresponding components
    private fun getFormData(view: View): ProjectViewModel {
        val projectName = view.findViewById<EditText>(R.id.edtProjectNameInput).text.toString()
        val description = view.findViewById<EditText>(R.id.edtProjectDescriptionInput).text.toString()
        val timeRange = view.findViewById<RangeSlider>(R.id.rangeProjectDailyHours)

        val timeRangeValues = timeRange.values
        val minHours = timeRangeValues[0].toInt()
        val maxHours = timeRangeValues[1].toInt()
        val tasks: MutableList<TaskViewModel> = mutableListOf()
        return ProjectViewModel(projectName, description, startDate, endDate, minHours, maxHours, tasks)
    }

    private fun addProject(project: ProjectViewModel) {
        //Performs validation.
        val snackbarMessage: String? = when {
            project.name.isBlank() -> "Project Name Required"
            project.description.isBlank() -> "Project Description Required"
            project.maximumDailyHours == 0 -> "Project Maximum Daily Hours Required"
            project.minimumDailyHours == 0 -> "Project Minimum Hours Required"
            project.startDate == null -> "Project Start Date Required"
            project.endDate == null -> "Project End Date Required"
            else -> null
        }

        if (snackbarMessage != null)
        {
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_LONG).show()
        }
        else
        {
        //Adds project to firestore.
        val db = Firebase.firestore
        var auth = Firebase.auth
        var uid = auth.uid
            db.collection("users").document(uid.toString()).collection("projects")
                .whereEqualTo("name", project.name)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val existingProjects = task.result?.documents ?: emptyList()
                        if (existingProjects.isNotEmpty()) {
                            Snackbar.make(requireView(), "A project with the same name already exists.", Snackbar.LENGTH_LONG).show()
                        } else {
                            // Adds project to Firestore
                            db.collection("users").document(uid.toString()).collection("projects")
                                .add(project)
                                .addOnCompleteListener { addTask ->
                                    if (addTask.isSuccessful) {
                                        val projectView = ViewProject.newInstance()
                                        requireActivity().supportFragmentManager.commit {
                                            setCustomAnimations(
                                                R.anim.fade_in,
                                                R.anim.fade_out
                                            )
                                            replace(R.id.flNavigation, projectView)
                                            addToBackStack(null)
                                        }
                                    } else {
                                        Snackbar.make(requireView(), addTask.exception?.message.toString(), Snackbar.LENGTH_LONG).show()
                                    }
                                }
                        }
                    } else {
                        Snackbar.make(requireView(), task.exception?.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }

        }

    }

    private fun createDatePicker(button: Button){
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates").setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
                .build()
        dateRangePicker.addOnPositiveButtonClickListener {
            var firstDate = it.first
            var secondDate = it.second
            startDate = Date(firstDate)
            endDate = Date(secondDate)
        }
        dateRangePicker.show(parentFragmentManager, "Tag")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectForm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProjectForm().apply {
                arguments = Bundle().apply {

                }
            }
    }
}