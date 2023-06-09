package data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R

class TaskViewAdapter(private val taskList: List<TaskViewModel>) : RecyclerView.Adapter<TaskViewAdapter.ViewHolder>() {
    class ViewHolder(taskView: View): RecyclerView.ViewHolder(taskView){

        val taskName = itemView.findViewById<TextView>(R.id.taskName)
        val taskDescription = itemView.findViewById<TextView>(R.id.taskDescription)
        val taskStartTime = itemView.findViewById<TextView>(R.id.taskStartTime)
        val taskNumberOfHours = itemView.findViewById<TextView>(R.id.taskNumberOfHours)
      //  val taskPhotoUrl = itemView.findViewById<TextView>(R.id.taskPhotoUrl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate( R.layout.project_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = taskList[position]
        holder.taskName.text = current.name
        holder.taskDescription.text = current.description
        holder.taskStartTime.text = current.startTime.toString()
        holder.taskNumberOfHours.text = current.numberOfHours.toString()
       // holder.taskPhotoUrl.text = current.photoUrl

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

}