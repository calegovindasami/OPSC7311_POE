package TaskForm

import android.app.Activity
import data.ProjectViewModel
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.opsc7311_poe.R
import com.example.opsc7311_poe.ViewTask
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import data.TaskViewModel
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PROJECTID = "projectId"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [TaskForm.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskForm : Fragment() {
    private lateinit var imgUri: Uri
    private lateinit var uploadedImgUri: Uri
    lateinit var startTime: Date

    private lateinit var auth: FirebaseAuth


    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                    imgUri = it.data?.data!!
                }

            }
    // TODO: Rename and change types of parameters
    private var projectId: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(ARG_PROJECTID)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_form, container, false)
        var btnStartTime = view.findViewById<Button>(R.id.btnTaskStartTime)

        btnStartTime.setOnClickListener() {
            createTimePicker(btnStartTime)
        }



        val btnUploadImg = view.findViewById<Button>(R.id.btnTaskUploadImg)
        btnUploadImg.setOnClickListener() {
            showImageSelector()
        }
        val btnSubmit = view.findViewById<Button>(R.id.btnTaskSubmit)
        btnSubmit.setOnClickListener() {
            auth = Firebase.auth
            val uid = auth.uid.toString()
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val now = Date()
            val fileName = formatter.format(now)
            val storageRef = FirebaseStorage.getInstance().getReference("$uid/$fileName")
            storageRef.putFile(imgUri).addOnCompleteListener() {
                if (it.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener {uri ->
                        uploadedImgUri = uri
                        uploadData(getFormData(view), view)
                    }
                }
            }

        }

        val btnTaskBack = view.findViewById<ImageButton>(R.id.btnTaskBack)

        btnTaskBack.setOnClickListener() {
            val viewTask = ViewTask.newInstance(projectId!!)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, viewTask).commit()
        }

        return view
    }

    private fun showImageSelector() {
        //contract.launch("image/*")
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }

    private fun getFormData(view: View): TaskViewModel {

        var name = view.findViewById<TextInputEditText>(R.id.edtTaskNameInput).text.toString()
        var desc = view.findViewById<TextInputEditText>(R.id.edtTaskDescriptionInput).text.toString()
        var numHours = view.findViewById<Slider>(R.id.rangeTaskNumHours).value.toInt()


        return TaskViewModel(name, desc, startTime, numHours, uploadedImgUri.toString())
    }

    private fun createTimePicker(button: Button){
        val isSystem24Hour = DateFormat.is24HourFormat(activity?.let{it})
        val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select start time")
                .setInputMode(INPUT_MODE_CLOCK)
                .build()
        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            val cal = Calendar.getInstance()
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            val date = "$day/$month/${year}T$hour:$minute"
            startTime = SimpleDateFormat("dd/MM/yyyy'T'HH:mm").parse(date)
            button.text = startTime.toString()
        }
        picker.show(parentFragmentManager, "tag")
    }

    private fun uploadData(task: TaskViewModel, contextView: View) {
        val db = Firebase.firestore
        auth = Firebase.auth
        val uid = auth.uid!!
        val docRef =  db.collection("users").document(uid).collection("projects").document(projectId!!)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val project = documentSnapshot.toObject<ProjectViewModel>()
            project!!.tasks!!.add(task)
            docRef.update("tasks", project!!.tasks!!).addOnSuccessListener {
                Toast.makeText(activity?.let{it}, "Task has been added!", Toast.LENGTH_LONG)
            }
                .addOnFailureListener() {
                    Toast.makeText(activity?.let{it}, "There has been an error", Toast.LENGTH_LONG)
                }

        }
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
        fun newInstance(projectId: String) =
            TaskForm().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROJECTID, projectId)
                }
            }
    }
}