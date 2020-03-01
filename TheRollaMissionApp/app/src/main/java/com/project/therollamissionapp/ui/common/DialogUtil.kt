package com.project.therollamissionapp.ui.common

import android.content.Context
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog
import com.project.therollamissionapp.R


object DialogUtil {
    fun makeErrorDialogue(
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
            setTitle(R.string.error)
            setPositiveButton(R.string.retry, onPositive)
            setNegativeButton(R.string.cancel, onNegative)
        }
        return builder?.create()
    }
}
