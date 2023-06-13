package Authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.opsc7311_poe.MainActivity
import com.example.opsc7311_poe.R
import com.example.opsc7311_poe.ViewProject
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val btnSubmit = view.findViewById<Button>(R.id.btnLoginSubmit)

        //Attempts to sign in user.
        btnSubmit.setOnClickListener() {
            auth = Firebase.auth
            val email = view.findViewById<EditText>(R.id.tiLoginEmail).text.toString()
            val password = view.findViewById<EditText>(R.id.tiLoginPassword).text.toString()


            if (email =="" || password == "") {
                Snackbar.make(requireView(), "Invalid fields", Snackbar.LENGTH_LONG).show()
            }
            else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
                    if (it.isSuccessful) {
                        val projectFragment = ViewProject.newInstance()
                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, projectFragment).commit()

                    }
                    else {
                        Snackbar.make(requireView(), it.exception!!.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        val btnSignUp = view.findViewById<TextView>(R.id.txtGoToSignUp)
        btnSignUp.setOnClickListener() {
            val registerFragment = RegisterFragment.newInstance()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, registerFragment).commit()
        }
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}