package ProjectForm

import Data.ProjectViewModel
import Data.TaskViewModel
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.opsc7311_poe.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
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

        val btnStartDate = view.findViewById<Button>(R.id.btnProjectStartDateInput)
        val btnEndDate = view.findViewById<Button>(R.id.btnProjectEndDateInput)

        val btnSubmit = view.findViewById<Button>(R.id.btnProjectSubmit)


        btnStartDate.setOnClickListener() {
            showStartDatePicker(btnStartDate)
        }

        btnEndDate.setOnClickListener() {
            showEndDatePicker(btnEndDate)
        }

        btnSubmit.setOnClickListener {
            val projectName = view.findViewById<EditText>(R.id.edtProjectNameInput).text.toString()
            val description = view.findViewById<EditText>(R.id.edtProjectDescriptionInput).text.toString()
            val minHours = Integer.parseInt(view.findViewById<EditText>(R.id.edtProjectMinDailyHoursInput).text.toString())
            val maxHours = Integer.parseInt(view.findViewById<EditText>(R.id.edtProjectMaxDailyHoursInput).text.toString())
//            val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//            val tdate = parser.parse("2018-12-14T09:55:00")
            val tasks = mutableListOf<TaskViewModel>()
//            val task = TaskViewModel("neww", "", tdate, 2, "https://firebasestorage.googleapis.com/v0/b/ontime-a3df1.appspot.com/o/st10083941%2FdVtiL2KihT64NCwHu3lA%2FFarm%20Central%20Background.jpg?alt=media&token=89ec95ae-93c7-4256-8bf4-3ab7e23a750e")
//            tasks.add(task)

            var project = ProjectViewModel(projectName, description, startDate, endDate, minHours, maxHours, tasks)

            addProject(project)
        }

        return view
    }

    private fun addProject(project: ProjectViewModel) {
        val db = Firebase.firestore
        db.collection("users").document("st10083941").collection("projects").add(project)
            .addOnSuccessListener {
                Toast.makeText(activity?.let{it}, "${project.name} has been added!", Toast.LENGTH_LONG)
            }
            .addOnFailureListener {
                Toast.makeText(activity?.let{it}, "${it.toString()}", Toast.LENGTH_LONG)
            }


    }

    private fun showEndDatePicker(button: Button) {
        endDate = createDatePicker(button)
    }

    private fun showStartDatePicker(button: Button) {
        startDate = createDatePicker(button)
    }

    private fun createDatePicker(button: Button): Date {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var setDate = Date()

        val datePickerDialog = activity?.let {
            DatePickerDialog(it, DatePickerDialog.OnDateSetListener {
                view, year, monthOfYear, dayOfMonth ->
                val date = "$dayOfMonth/$monthOfYear/$year"
                button.text = date
                setDate = (SimpleDateFormat("dd/MM/yyyy")).parse(date)
            }, year, month, day)
        }

        datePickerDialog!!.show()
        return setDate
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