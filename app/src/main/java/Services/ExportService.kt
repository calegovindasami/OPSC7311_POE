package Services

/*import android.content.Context
import android.os.Environment
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.IOException

class ExportService(private val context: Context) {

    fun createFile(){
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet 1")

        // Write data to cells
        val row: Row = sheet.createRow(0)
        val cell: Cell = row.createCell(0)
        cell.setCellValue("Hello")
        row.createCell(1).setCellValue("World")


        val filePath = "${context.getExternalFilesDir(null)}/example.xlsx"

        try {
            FileOutputStream(filePath).use { fileOut ->
                workbook.write(fileOut)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

}*/

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import data.TaskViewModel
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.IOException

class ExportService(private val context: Context) {

    fun createFile(tasks:MutableList<TaskViewModel>){

        val workbook: Workbook = HSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet 1")

        var r = 0


       tasks.forEach(){
           t->
           var c = 0

           val row: Row = sheet.createRow(r)
           val cell: Cell = row.createCell(c)

           cell.setCellValue(t.name)
           row.createCell(c+1).setCellValue(t.startTime)
           row.createCell(c+2).setCellValue(t.numberOfHours.toString())

           r++

       }



        // Write data to cells


        val filePath = "${context.getExternalFilesDir(null)}/example.xlsx"

        try {
            FileOutputStream(filePath).use { fileOut ->
                workbook.write(fileOut)
            }

            openExcelFile(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun openExcelFile(filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse("file://$filePath"), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}