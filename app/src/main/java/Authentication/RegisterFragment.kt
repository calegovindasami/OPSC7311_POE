package Authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.opsc7311_poe.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
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
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        val view = inflater.inflate(R.layout.fragment_register, container, false)


        val btnRegister = view.findViewById<Button>(R.id.btnRegisterSubmit)
        btnRegister.setOnClickListener() {

            //Registers user
            auth = Firebase.auth
            val email = view.findViewById<EditText>(R.id.tiRegisterEmail).text.toString()
            val password = view.findViewById<EditText>(R.id.tiRegisterPassword).text.toString()
            //Validates
            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(context, "Invalid fields", Toast.LENGTH_LONG)
            }
            else   {
                auth.createUserWithEmailAndPassword(email.trim(), password)
                    .addOnCompleteListener() {
                        if (it.isSuccessful) {
                            Snackbar.make(requireView(), "Successfully registered!", Snackbar.LENGTH_LONG)
                            clearFields(view)
                        }
                        else if(it.isCanceled) {
                            Snackbar.make(requireView(), it.exception?.message.toString(), Snackbar.LENGTH_LONG)


                        }
                    }
            }
        }


        val btnLogin = view.findViewById<TextView>(R.id.txtGoToLogin)
        btnLogin.setOnClickListener() {
            navigateToLogin()
        }


        return view
    }

    private fun navigateToLogin() {
        val loginFragment = LoginFragment.newInstance()
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, loginFragment).commit()
    }
    private fun clearFields(view: View) {
        view.findViewById<EditText>(R.id.tiRegisterEmail).text.clear()
        view.findViewById<EditText>(R.id.tiRegisterPassword).text.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}