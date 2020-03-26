package com.project.therollamissionapp.ui.registration

import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout
import com.project.therollamissionapp.R
import com.project.therollamissionapp.databinding.FragmentRegistrationBinding
import com.project.therollamissionapp.ui.common.Helpers
import java.text.ParseException

class FormValidator (private val context: Context,
                     private val binding: FragmentRegistrationBinding,
                     private val viewModel: RegistrationViewModel) {
    private val errorColor = ContextCompat.getColor(context, R.color.error)
    private val defaultColor = binding.textTitle.currentTextColor

    fun validateForm(): Boolean {
        return fieldsFilled() && validateBirthdate()
    }

    private fun fieldsFilled(): Boolean {
        var allFilled = true
        allFilled = textInputsFilled() && allFilled
        allFilled = radiosChosen() && allFilled
        allFilled = consentGiven() && allFilled
        allFilled = pictureTaken() && allFilled
        return hasSigned() && allFilled
    }

    private fun textInputsFilled(): Boolean {
        var inputs = arrayOf(
            binding.layoutName, binding.layoutBirthdate, binding.layoutContactNumber,
            binding.layoutGender, binding.layoutContactNumber, binding.layoutDurationHomeless
        )
        if (binding.radioFromRollaN.isChecked) {
            inputs += binding.layoutCity
            inputs += binding.layoutReason
        }
        var allFilled = true
        for (input in inputs) {
            allFilled = textInputFilled(input) && allFilled
        }
        return allFilled
    }

    private fun textInputFilled(field: TextInputLayout): Boolean {
        val editText = field.editText as EditText
        if (TextUtils.isEmpty(editText.text.toString())) {
            field.error = context.getString(R.string.required)
            return false
        } else {
            field.error = ""
            return true
        }
    }

    private fun radiosChosen(): Boolean {
        val groups = arrayOf(
            binding.layoutRollaWhenBecameHomeless, binding.layoutViolence,
            binding.layoutVeteran, binding.layoutOffender
        )
        var allSelected = true
        for (group in groups) {
            allSelected = radioChosen(group) && allSelected
        }
        return allSelected
    }

    private fun radioChosen(layout: LinearLayout): Boolean {
        val buttonGroup = layout.children.last() as RadioGroup
        val question = layout.children.first() as TextView
        if (buttonGroup.checkedRadioButtonId <= 0) {
            question.setTextColor(errorColor)
            return false
        } else {
            question.setTextColor(defaultColor)
            return true
        }
    }

    private fun consentGiven(): Boolean {
        if (binding.checkboxConsent1.isChecked && binding.checkboxConsent2.isChecked &&
                binding.checkboxConsent3.isChecked) {
            binding.textConsent.setTextColor(defaultColor)
            return true
        } else {
            binding.textConsent.setTextColor(errorColor)
            return false
        }
    }

    private fun pictureTaken(): Boolean {
        if (viewModel.pictureTaken()) {
            val color = ContextCompat.getColor(context, R.color.colorPrimary)
            binding.buttonTakePicture.setBackgroundColor(color)
            return true
        } else {
            binding.buttonTakePicture.setBackgroundColor(errorColor)
            return false
        }
    }

    private fun hasSigned(): Boolean {
        if (viewModel.hasSigned()) {
            binding.textSignature.setTextColor(defaultColor)
            return true
        } else {
            binding.textSignature.setTextColor(errorColor)
            return false
        }
    }

    private fun validateBirthdate(): Boolean {
        try {
            Helpers.formatBirthDate(binding.editBirthdate.text.toString())
            return true
        } catch (e: ParseException) {
            binding.editBirthdate.error = "Invalid birthdate"
            return false
        }
    }
}