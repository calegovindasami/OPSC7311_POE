package Data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R

class HoursViewAdapter (private val hoursList: List<HoursViewModel>) : RecyclerView.Adapter<HoursViewAdapter.ViewHolder>() {
    class ViewHolder(hoursView: View): RecyclerView.ViewHolder(hoursView){
        val hoursProjectName = itemView.findViewById<TextView>(R.id.hoursProjectName)
        val totalHours = itemView.findViewById<TextView>(R.id.totalHours)
        val totalTasks = itemView.findViewById<TextView>(R.id.totalTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hours_card,parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HoursViewAdapter.ViewHolder, position: Int) {

        val current = hoursList[position]

        holder.hoursProjectName.text = current.projectName
        holder.totalHours.text = current.totalHours.toString()
        holder.totalTasks.text = current.numberOfTasks.toString()

    }

    override fun getItemCount(): Int {
        return hoursList.size
    }


}