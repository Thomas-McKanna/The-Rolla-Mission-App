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
        return fieldsFilled() && validateName() && validateBirthdate()
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
            field.error = null
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
            binding.checkboxConsent3.isChecked
        ) {
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

    private fun validateName(): Boolean {
        val nameRegex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*\$".toRegex()
        val name = viewModel.name.value.toString()
        val nameEditText = binding.editName
        val matches = nameRegex.matches(name)
        if (!matches) {
            nameEditText.error = context.getString(R.string.invalid_name)
        } else {
            nameEditText.error = null
        }
        return matches
    }

    private fun validateBirthdate(): Boolean {
        val birthDate = viewModel.birthDate.value.toString()
        val birthDateRegex = "^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d\$".toRegex()
        if (birthDateRegex.matches(birthDate)) {
            binding.editBirthdate.error = null
            return true
        } else {
            binding.editBirthdate.error = context.getString(R.string.invalid_birthdate)
            return false
        }
    }
}