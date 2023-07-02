package Services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import data.TaskViewModel
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExportService(private val context: Context) {

    fun createFile(tasks: MutableList<TaskViewModel>) {

        val workbook: Workbook = HSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet 1")

        var r = 0


        tasks.forEach() { t ->
            var c = 0

            val row: Row = sheet.createRow(r)
            val cell: Cell = row.createCell(c)

            cell.setCellValue(t.name)
            row.createCell(c + 1).setCellValue(t.startTime)
            row.createCell(c + 2).setCellValue(t.numberOfHours.toString())

            r++

        }

        val path2 = getDownloadPath(context) + "/Sheet 1.xlsx"
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

}