package TaskForm

import Data.TaskViewModel
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import com.example.opsc7311_poe.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [TaskForm.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskForm : Fragment() {
    lateinit var imgUri: Uri
    lateinit var startTime: Date

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {

        val imgUri = it
    }
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
        val view = inflater.inflate(R.layout.fragment_task_form, container, false)

        var btnGetDate = view.findViewById<Button>(R.id.btnTaskStartDateInput)

        btnGetDate.setOnClickListener() {
            startTime = createTimePicker(btnGetDate)
        }

        val btnUploadImage = view.findViewById<Button>(R.id.btnTaskUploadImageInput)
        btnUploadImage.setOnClickListener() {
            showImageSelector()
        }

        val btnSubmit = view.findViewById<Button>(R.id.btnTaskSubmit)

        btnSubmit.setOnClickListener() {

        }

        return view
    }

    private fun showImageSelector() {
        contract.launch("image/*")
    }

    private fun getFormData(view: View): TaskViewModel {

        var name = view.findViewById<EditText>(R.id.edtTaskNameInput).text.toString()
        var desc = view.findViewById<EditText>(R.id.edtTaskDescriptionInput).text.toString()
        var numHours = Integer.parseInt(view.findViewById<EditText>(R.id.edtTaskNumHoursInput).text.toString())

        return TaskViewModel(name, desc, startTime, numHours, imgUri.toString())
    }

    private fun createTimePicker(button: Button): Date {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var setDate = Date()

        val timePickerDialog = activity?.let {
            TimePickerDialog(it, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var hour = hourOfDay
                var dateTime = "$year-$month-${day}T$hourOfDay:$minute:00"
                setDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateTime)

            }, hour, min,false)
        }

        timePickerDialog!!.show()
        return setDate
    }

    private fun uploadData(task: TaskViewModel) {

    }

    private fun uploadImage() {

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskForm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            TaskForm().apply {
                arguments = Bundle().apply {

                }
            }
    }
}