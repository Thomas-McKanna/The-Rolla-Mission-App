package com.project.therollamissionapp.ui.checkin

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.databinding.DialogueCheckInConfirmationBinding

class CheckInConfirmationDialogue (
    private val patron: Patron,
    private val positiveListener: DialogInterface.OnClickListener
): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            val builder = AlertDialog.Builder(it,
                R.style.ThemeOverlay_MaterialComponents_Dialog_LargeTitle)
            val inflater = requireActivity().layoutInflater;
            val root = inflater.inflate(R.layout.dialogue_check_in_confirmation, null)
            val binding = DialogueCheckInConfirmationBinding.bind(root)
            binding.patron = patron
            builder.setTitle(R.string.is_this_you)
            builder.setView(binding.root)
                .setPositiveButton(R.string.yes, positiveListener)
                .setNegativeButton(R.string.no, null)
            builder.create()
        }
    }
}