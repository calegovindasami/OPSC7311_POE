package data

import Data.ProjectViewModel
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R

class ProjectAdapter(private val projectList : ArrayList<ProjectViewModel>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAdapter.ProjectViewHolder {
        val itemView = View.inflate(parent.context, R.layout.activity_main, null)
        return ProjectViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ProjectViewHolder, position: Int) {
        val current = projectList[position]
        holder.projectName.text = current.name
        holder.projectDescription.text = current.description
        holder.projectStartDate.text = current.startDate.toString()
        holder.projectEndDate.text = current.endDate.toString()
        holder.projectMinimumDailyHours.text = current.minimumDailyHours.toString()
        holder.projectMaximumDailyHours.text = current.maximumDailyHours.toString()
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    class ProjectViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Need a screen to use this method
        val projectName = itemView.findViewById<TextView>(R.id.projectName)
        val projectDescription = itemView.findViewById<TextView>(R.id.projectDescription)
        val projectStartDate = itemView.findViewById<TextView>(R.id.projectStartDate)
        val projectEndDate = itemView.findViewById<TextView>(R.id.projectEndDate)
        val projectMinimumDailyHours = itemView.findViewById<TextView>(R.id.projectMinimumDailyHours)
        val projectMaximumDailyHours = itemView.findViewById<TextView>(R.id.projectMaximumDailyHours)
    }
}


