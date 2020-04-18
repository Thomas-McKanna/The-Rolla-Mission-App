package com.project.therollamissionapp.ui.common

import android.content.Context
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog
import com.project.therollamissionapp.R

object DialogUtil {
    fun makeErrorDialog(
        context: Context?,
        onPositive: OnClickListener?,
        onNegative: OnClickListener?
    ): AlertDialog? {
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.apply {
            setIcon(R.drawable.ic_warning)
            setMessage(R.string.error_message)
            setTitle(R.string.error_title)
            setPositiveButton(R.string.retry, onPositive)
            setNegativeButton(R.string.cancel, onNegative)
        }
        return builder?.create()
    }

    fun makeWarningDialog(
        context: Context?,
        onPositive: OnClickListener?,
        onNegative: OnClickListener?
    ): AlertDialog? {
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.apply {
            setIcon(R.drawable.ic_warning)
            setMessage(R.string.warning_message)
            setTitle(R.string.warning)
            setPositiveButton(R.string.yes, onPositive)
            setNegativeButton(R.string.cancel, onNegative)
        }
        return builder?.create()
    }

    fun makeSuccessDialog(
        context: Context?,
        onPositive: OnClickListener?,
        onNegative: OnClickListener?
    ): AlertDialog? {
        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.apply {
            setMessage(R.string.registration_success_message)
            setTitle(R.string.registration_success_title)
            setPositiveButton(R.string.yes, onPositive)
            setNegativeButton(R.string.no, onNegative)
        }
        return builder?.create()
    }
}
