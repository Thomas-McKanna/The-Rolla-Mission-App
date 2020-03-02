package com.project.therollamissionapp.ui.registration

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.project.therollamissionapp.R
import java.util.*

class DatePickerFragment (
    private val viewModel: RegistrationViewModel
): DialogFragment(), SupportedDatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return SupportedDatePickerDialog(
            context = context!!,
            themeResId = R.style.SpinnerDatePickerDialogTheme,
            listener = this,
            year = year,
            monthOfYear = month,
            dayOfMonth = day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        viewModel.setBirthDate(year, month, day)
    }
}