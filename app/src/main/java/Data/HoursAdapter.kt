package Data

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R

class HoursAdapter (private val hoursList: MutableList<HoursViewModel>) : RecyclerView.Adapter<HoursAdapter.HoursViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursAdapter.HoursViewHolder {
        val itemView = View.inflate(parent.context, R.layout.activity_main, null)
        return HoursAdapter.HoursViewHolder(parent)
    }

    override fun onBindViewHolder(holder: HoursAdapter.HoursViewHolder, position: Int) {
        val current = hoursList[position]
        holder.hoursProjectName.text = current.projectName
        holder.totalHours.text = current.totalHours.toString()
        holder.totalTasks.text = current.numberOfTasks.toString()

    }

    override fun getItemCount(): Int {
        return hoursList.size
    }

    class HoursViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // TODO Need a screen to use this method
        val hoursProjectName = itemView.findViewById<TextView>(R.id.hoursProjectName)
        val totalHours = itemView.findViewById<TextView>(R.id.totalHours)
        val totalTasks = itemView.findViewById<TextView>(R.id.totalTasks)


    }
}