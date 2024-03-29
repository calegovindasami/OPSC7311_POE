package data


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class
ProjectViewAdapter(private val projectList: List<ProjectViewModel>) : RecyclerView.Adapter<ProjectViewAdapter.ViewHolder>() {

    private lateinit var mlistener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mlistener = listener
    }

    class ViewHolder(projectView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(projectView) {

        val name: TextView = projectView.findViewById(R.id.projectName)
        val desc: TextView = projectView.findViewById(R.id.projectDescription)
        val startDate: TextView = projectView.findViewById(R.id.projectStartDate)
        val endDate: TextView = projectView.findViewById(R.id.projectEndDate)
        val minHours: TextView = projectView.findViewById(R.id.projectMinimumDailyHours)
        val maxHours: TextView = projectView.findViewById(R.id.projectMaximumDailyHours)

        init{
            projectView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_card, parent, false)
        return ViewHolder(view,mlistener)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val projectViewModel = projectList[position]

        holder.name.text = projectViewModel.name
        holder.desc.text = projectViewModel.description


        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


        holder.startDate.text = "From: ${formatter.format(projectViewModel.startDate)} "
        holder.endDate.text = "To: ${formatter.format(projectViewModel.endDate)} "

        holder.minHours.text = "Daily Hours: " + projectViewModel.minimumDailyHours.toString() + " - "
        holder.maxHours.text = projectViewModel.maximumDailyHours.toString()

    }



}