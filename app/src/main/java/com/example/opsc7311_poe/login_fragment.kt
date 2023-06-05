package com.example.opsc7311_poe

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [login_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class login_fragment : Fragment() {
    lateinit var etEmail: EditText

    private lateinit var etPass: EditText
    lateinit var btnLogin: Button
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
        val view = inflater.inflate(R.layout.fragment_login_fragment, container, false)


        //initialize firebase auth
        auth = Firebase.auth


        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPass = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            login()
        }

        return view

    }



private fun login() {
    val email = etEmail.text.toString()
    val pass = etPass.text.toString()
}

    private fun signInUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email.trim(), pass)
            .addOnCompleteListener(activity?.let { it }) { it ->
                if (it.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")

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
         * @return A new instance of fragment login_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            login_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}