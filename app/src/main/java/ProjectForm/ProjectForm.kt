package ProjectForm

import data.ProjectViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.example.opsc7311_poe.R
import com.example.opsc7311_poe.ViewProject
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
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

private var startDate: Date = Date()
private var endDate: Date = Date()



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

        val btnBack = view.findViewById<Button>(R.id.btnProjectBack)
        btnBack.setOnClickListener() {
            val projectView = ViewProject.newInstance()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, projectView).commit()
        }

        btnSubmit.setOnClickListener {
            addProject(getFormData(view))
        }

        return view
    }

    private fun getFormData(view: View): ProjectViewModel {
        val projectName = view.findViewById<EditText>(R.id.edtProjectNameInput).text.toString()
        val description = view.findViewById<EditText>(R.id.edtProjectDescriptionInput).text.toString()
        val timeRange = view.findViewById<RangeSlider>(R.id.rangeProjectDailyHours)
        val minHours = timeRange.valueFrom.toInt()
        val maxHours = timeRange.valueTo.toInt()
        return ProjectViewModel(projectName, description, startDate, endDate, minHours, maxHours, null)
    }

    private fun addProject(project: ProjectViewModel) {
        val db = Firebase.firestore
        var auth = Firebase.auth
        var uid = auth.uid
        db.collection("users").document(uid.toString()).collection("projects").add(project).addOnCompleteListener() {
            if (it.isSuccessful) {
                Snackbar.make(requireView(), "Project added.", Snackbar.LENGTH_LONG)
            }
            else {
                Snackbar.make(requireView(), it.exception?.message.toString(), Snackbar.LENGTH_LONG)
            }
        }
    }




    private fun createDatePicker(button: Button){
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates").setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
                .build()

        dateRangePicker.addOnPositiveButtonClickListener {
            val dates = dateRangePicker.selection
            startDate = Date(dates!!.first)
            endDate = Date(dates!!.second)

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