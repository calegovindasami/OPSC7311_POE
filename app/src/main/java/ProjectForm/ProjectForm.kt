package ProjectForm

import android.content.ContentValues.TAG
import data.ProjectViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.opsc7311_poe.R
import com.example.opsc7311_poe.ViewProject
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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

//User needs to select one of these values for their preferred time. These times are limited to two hour intervals as Firebase allows a maximum of 10 messages per app (free).
private var notificationTimes = mutableListOf(6, 8, 10, 12, 14, 16, 18, 20, 22, 24)



class ProjectForm : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        val btnSubmit = view.findViewById<Button>(R.id.btnProjectSubmit)

        val btnBack = view.findViewById<ImageButton>(R.id.btnProjectBack)
        btnBack.setOnClickListener() {
            val projectView = ViewProject.newInstance()
            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                replace(R.id.flNavigation, projectView)
                addToBackStack(null)
            }
        }

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
        db.collection("users").document(uid.toString()).collection("projects").add(project).addOnCompleteListener() {
            if (it.isSuccessful) {
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
                Snackbar.make(requireView(), it.exception?.message.toString(), Snackbar.LENGTH_LONG)
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


    //Notify hour must be a value from the notificationTimes list
    private fun subscribeToNotification(notifyHour: Int) {
        Firebase.messaging.subscribeToTopic(notifyHour.toString())
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
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