package data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe.R
import java.util.concurrent.Executors
import data.TaskViewModel
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class TaskViewAdapter(private val taskList: MutableList<TaskViewModel>) : RecyclerView.Adapter<TaskViewAdapter.ViewHolder>() {
    class ViewHolder(taskView: View): RecyclerView.ViewHolder(taskView){

        val taskName = itemView.findViewById<TextView>(R.id.taskName)
        val taskDescription = itemView.findViewById<TextView>(R.id.taskDescription)
        val taskStartTime = itemView.findViewById<TextView>(R.id.taskStartTime)
        val taskNumberOfHours = itemView.findViewById<TextView>(R.id.taskNumberOfHours)
        val taskPhoto = itemView.findViewById<ImageView>(R.id.imgTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate( R.layout.task_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = taskList[position]
        holder.taskName.text = current.name
        holder.taskDescription.text = current.description

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        holder.taskStartTime.text = current.startTime.toString()
        holder.taskNumberOfHours.text = current.numberOfHours.toString()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        if (current.photoUrl != null) {
            executor.execute {
                val image = downloadImage(current.photoUrl!!)
                handler.post {
                    holder.taskPhoto.setImageBitmap(image)
                    holder.taskPhoto.adjustViewBounds = true
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    private fun downloadImage(string: String): Bitmap? {
        val url: URL = URL(string)
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}