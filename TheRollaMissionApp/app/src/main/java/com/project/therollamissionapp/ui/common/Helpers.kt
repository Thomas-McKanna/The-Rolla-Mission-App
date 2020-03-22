package com.project.therollamissionapp.ui.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import com.project.therollamissionapp.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

    fun getDateHomeless(context: Context, duration: String?): String {
        var zdt = ZonedDateTime.now()
        zdt = when(duration) {
            context.getString(R.string.less_1_month) -> zdt
            context.getString(R.string.months_1to3) -> zdt.minus(1, ChronoUnit.MONTHS)
            context.getString(R.string.months_3to6) -> zdt.minus(3, ChronoUnit.MONTHS)
            context.getString(R.string.months_6to12) -> zdt.minus(6, ChronoUnit.MONTHS)
            context.getString(R.string.years_1to2) -> zdt.minus(1, ChronoUnit.YEARS)
            else -> zdt.minus(2, ChronoUnit.YEARS)
        }
        return zdt.format(DateTimeFormatter.ISO_INSTANT)
    }
}