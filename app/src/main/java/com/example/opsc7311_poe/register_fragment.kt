package com.example.opsc7311_poe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [register_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class register_fragment : Fragment() {
    lateinit var etEmail: EditText
    lateinit var etConfEmail: EditText
    lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_fragment, container, false)

        val etEmail = view.findViewById<EditText>(R.id.etRegEmail)
        val etConfEmail = view.findViewById<EditText>(R.id.etConfirmEmail)
        val etPass = view.findViewById<EditText>(R.id.etPassword)
        val etConfPass = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)

        btnRegister
            .setOnClickListener {
                signUpUser()
            }



        return view
    }


    private fun signUpUser() {
        val email = etEmail.text.toString()
        val confirmEmail = etConfEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()

        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

    }

        private fun register(email: String, pass: String) {
            auth.createUserWithEmailAndPassword(email.trim(), pass.trim())
                .addOnCompleteListener(activity?.let { it }) {

                    if (it.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")

                    } else {
                        Log.w(TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show

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
         * @return A new instance of fragment register_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            register_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}