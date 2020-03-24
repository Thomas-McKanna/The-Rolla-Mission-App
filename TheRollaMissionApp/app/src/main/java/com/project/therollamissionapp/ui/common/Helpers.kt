package com.project.therollamissionapp.ui.common

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import com.project.therollamissionapp.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object Helpers {
    fun <T> setValueIfDifferent(ld: MutableLiveData<T>, value: T) {
        if (ld.value != value) {
            ld.value = value
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun formatDateHomeless(context: Context, duration: String?): String {
        var zdt = ZonedDateTime.now()
        zdt = when(duration) {
            context.getString(R.string.less_1_month) -> zdt
            context.getString(R.string.months_1to3) -> zdt.minus(1, ChronoUnit.MONTHS)
            context.getString(R.string.months_3to6) -> zdt.minus(3, ChronoUnit.MONTHS)
            context.getString(R.string.months_6to12) -> zdt.minus(6, ChronoUnit.MONTHS)
            context.getString(R.string.years_1to2) -> zdt.minus(12, ChronoUnit.MONTHS)
            else -> zdt.minus(24, ChronoUnit.MONTHS)
        }
        return zdt.format(DateTimeFormatter.ISO_INSTANT)
    }

    fun formatBirthDate(birthDate: String?): String {
        val inputFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val outputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var formattedBirthDate = ""
        birthDate?.apply {
            val date = inputFormatter.parse(this)
            if (date != null) {
                formattedBirthDate = outputFormatter.format(date)
            }
        }
        return formattedBirthDate
    }

    fun compressAndSaveBitmap(dir: File, child: String, bitmap: Bitmap?,
                              format: Bitmap.CompressFormat): File {
        val file = File(dir, child)
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap?.compress(format, 0, bos)
        val fos = FileOutputStream(file)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        return file
    }
}