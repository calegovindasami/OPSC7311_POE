package com.example.opsc7311_poe

import ProjectForm.ProjectForm
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import data.ProjectViewAdapter
import data.ProjectViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewProject.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewProject : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var db: FirebaseFirestore = Firebase.firestore

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

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
        val view = inflater.inflate(R.layout.fragment_view_project, container, false)



        val btnAddProject = view.findViewById<FloatingActionButton>(R.id.btnAddProject)

        btnAddProject.setOnClickListener() {
            val projectForm = ProjectForm.newInstance()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, projectForm).commit()
        }


        auth = Firebase.auth
        val uid = auth.uid!!
        var projectList: MutableList<ProjectViewModel> = mutableListOf()
        val docRef =  db.collection("users").document(uid).collection("projects")
        docRef.get().addOnCompleteListener() {
            if (it.isSuccessful) {
                var projects = it.result.documents
                for (p in projects) {
                    var project = p.toObject<ProjectViewModel>()
                    projectList.add(project!!)
                }
                recyclerView = view.findViewById<RecyclerView>(R.id.project_view)
                recyclerView.layoutManager = LinearLayoutManager(context)
                val adapter = ProjectViewAdapter(projectList)
                adapter.setOnItemClickListener(object: ProjectViewAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        Snackbar.make(requireView(), projectList[position].name, Snackbar.LENGTH_LONG).show()
                    }
                })
                recyclerView.adapter = adapter

                val x = "hit"
            }

            else if (it.isCanceled) {
                Snackbar.make(requireView(), it.exception!!.message.toString(), Snackbar.LENGTH_LONG).show()
            }


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
         * @return A new instance of fragment ViewProject.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ViewProject().apply {
                arguments = Bundle().apply {

                }
            }
    }
}