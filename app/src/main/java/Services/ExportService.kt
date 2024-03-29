package Services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.opsc7311_poe.R
import data.TaskViewModel
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook
import org.apache.poi.hssf.usermodel.HSSFFont
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
* Code Attribution for Excel Feature
* Source: https://www.baeldung.com/kotlin/excel-read-write
*/
class ExportService(private val context: Context) {

    //Method creates an excel file and stores all of the users tasks in it, and downloads file to their phone
    fun createFile(tasks: MutableList<TaskViewModel>) {

        val workbook: Workbook = HSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Task Sheet")

        var r = 1

        val headingRow = sheet.createRow(0)
        val headingCell = headingRow.createCell(0)
        headingCell.setCellValue("Task Name")
        headingRow.createCell( 1).setCellValue("Date")
        headingRow.createCell(2).setCellValue("Hours Worked")

        tasks.forEach() { t ->
            var c = 0

            val row: Row = sheet.createRow(r)
            val cell: Cell = row.createCell(c)

            cell.setCellValue(t.name)
            val calendar = Calendar.getInstance()
            calendar.time = t.startTime
            val taskDate = calendar.get(Calendar.DAY_OF_MONTH).toString() + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR)
            row.createCell(c + 1).setCellValue(taskDate)
            row.createCell(c + 2).setCellValue(t.numberOfHours.toString())

            r++

        }

        val path2 = getDownloadPath(context) + createFileNameWithDateTime()
        // Write data to cells


        val filePath = path2!!

        try {
            val file = File(filePath)
            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            fileOutputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            workbook.close()
        }

    }

    //Method will retrieve the download path for the users phone
    private fun getDownloadPath(context: Context): String? {
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (downloadFolder != null) {
            if (!downloadFolder.mkdirs() && !downloadFolder.exists()) {
                return null
            }
            return downloadFolder.absolutePath
        } else {
            val filesDir = context.getExternalFilesDir(null)
            if (filesDir != null) {
                return filesDir.absolutePath
            }
        }
        return null
    }


    //Method creates a new unique file name for the new sheet that a user exports, based on the date
    fun createFileNameWithDateTime(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())
        return "/Sheet_$currentDateAndTime.xlsx"
    }

}