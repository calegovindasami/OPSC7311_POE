package Services

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*


//This class is used to display the datepicker pop up

class DatePickerPopUp : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var startDate: Date? = null
    private var endDate: Date? = null



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int){

        val selectedStartDate = Calendar.getInstance()
        selectedStartDate.set(year, month, dayOfMonth)

    }

}