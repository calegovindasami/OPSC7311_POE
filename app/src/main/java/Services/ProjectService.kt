package Services

import Data.ProjectViewModel
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProjectService {
    private var db: FirebaseFirestore = Firebase.firestore


    suspend fun getProjects(userId: String, view: View): MutableList<ProjectViewModel> {
        var projectList:MutableList<ProjectViewModel> = mutableListOf()
        val docRef =  db.collection("users").document(userId).collection("projects")
        docRef.get().addOnSuccessListener {
                projects ->
            for (proj in projects) {
                var project = proj.toObject<ProjectViewModel>()
                projectList.add(project)
            }
        }
            .addOnFailureListener() {
                Snackbar.make(view, "Error loading projects.", Snackbar.LENGTH_LONG)
            }

        return projectList
    }

    public fun getProject(userId: String): ProjectViewModel {
        var project: ProjectViewModel = ProjectViewModel()
        val docRef =  db.collection("users").document(userId).collection("projects").document("jV6Qj5X4Yat2lgHwYm2W")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val proj = documentSnapshot.toObject<ProjectViewModel>()
            project = proj!!
        }
            .addOnFailureListener() {
                //TODO
            }
        return project
    }

    private fun addProject(userId: String, project: ProjectViewModel) {
        db.collection("users").document(userId).collection("projects").add(project)
            .addOnSuccessListener {
                //TODO
            }
            .addOnFailureListener {
                //TODO
            }
    }
}