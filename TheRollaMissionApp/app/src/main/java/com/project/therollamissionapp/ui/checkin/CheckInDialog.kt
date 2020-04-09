package com.project.therollamissionapp.ui.checkin

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.databinding.DialogCheckInBinding
import com.project.therollamissionapp.ui.common.ImageUtils.takeImage

class CheckInDialog (
    private val viewModel: CheckInViewModel,
    private val patron: Patron,
    private val positiveListener: DialogInterface.OnClickListener
): DialogFragment() {
    val REQUEST_RETAKE_PHOTO = 1

    private lateinit var binding: DialogCheckInBinding

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_RETAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            binding.patron = patron // Refresh the image
            viewModel.updateHeadshot(patron)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            val builder = AlertDialog.Builder(it,
                R.style.ThemeOverlay_MaterialComponents_Dialog_LargeTitle)
            val inflater = requireActivity().layoutInflater;
            val root = inflater.inflate(R.layout.dialog_check_in, null)
            binding = DialogCheckInBinding.bind(root)
            binding.patron = patron
            builder.setTitle(R.string.is_this_you)
            builder.setView(binding.root)
                .setPositiveButton(R.string.yes, positiveListener)
                .setNegativeButton(R.string.no, null)
                .setNeutralButton(R.string.retake, null)
            builder.create()
        }
    }

    override fun onResume() {
        super.onResume()
        // The following code suppresses the default behavior of the dialog dismissing upon
        // clicking on the "retake" button.
        val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
        button.setOnClickListener {
            takeImage(this, patron.id, REQUEST_RETAKE_PHOTO)
        }
    }
}