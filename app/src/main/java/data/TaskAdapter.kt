package data

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R

class TaskAdapter(private val taskList: MutableList<TaskViewModel>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val itemView = View.inflate(parent.context, R.layout.task_list_item, null)
        return TaskAdapter.TaskViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val current = taskList[position]
        holder.taskName.text = current.name
        holder.taskDescription.text = current.description
        holder.taskStartTime.text = current.startTime.toString()
        holder.taskNumberOfHours.text = current.numberOfHours.toString()
        holder.taskPhotoUrl.text = current.photoUrl

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // TODO Need a screen to use this method
        val taskName = itemView.findViewById<TextView>(R.id.taskName)
        val taskDescription = itemView.findViewById<TextView>(R.id.taskDescription)
        val taskStartTime = itemView.findViewById<TextView>(R.id.taskStartTime)
        val taskNumberOfHours = itemView.findViewById<TextView>(R.id.taskNumberOfHours)
        val taskPhotoUrl = itemView.findViewById<TextView>(R.id.taskPhotoUrl)

    }
}