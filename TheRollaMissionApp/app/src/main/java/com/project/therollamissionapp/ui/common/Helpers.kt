package com.project.therollamissionapp.ui.common

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData

object Helpers {
    fun <T> setValueIfDifferent(ld: MutableLiveData<T>, value: T) {
        if (ld.value != value) {
            ld.value = value
        }
    }

    fun hideKeyboard(view: View?) {
        val context = view?.context
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.apply {
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}