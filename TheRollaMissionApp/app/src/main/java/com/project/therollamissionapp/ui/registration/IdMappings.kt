package com.project.therollamissionapp.ui.registration

import androidx.lifecycle.MutableLiveData
import com.project.therollamissionapp.R

object IdMappings {
    fun getGender(id: Int): String {
        return when (id) {
            R.id.radio_gender_male -> "Male"
            R.id.radio_gender_female -> "Female"
            R.id.radio_gender_other -> "Other"
            R.id.radio_gender_no_ans -> "N/A"
            else -> ""
        }
    }

    fun getReasonRolla(id: Int): String {
        return when (id) {
            R.id.radio_reason_family -> "Family in the area"
            R.id.radio_reason_work -> "Work/job offer"
            R.id.radio_reason_school -> "School"
            R.id.radio_reason_relationship -> "Relationship"
            R.id.radio_reason_hospital -> "Hospital/Medical care"
            R.id.radio_reason_mission -> "The Rolla Mission"
            R.id.radio_reason_other -> "Other"
            else -> ""
        }
    }

    fun getTimeHomeless(id: Int): String {
        return when (id) {
            R.id.radio_homeless_1to3month -> "1-3 months"
            R.id.radio_homeless_4to6month -> "4-6 months"
            R.id.radio_homeless_6to12month -> "6-12 months"
            R.id.radio_homeless_1to2year -> "1-2 years"
            R.id.radio_homeless_2plusyear -> "2+ years"
            else -> ""
        }
    }

    fun getVeteran(id: Int): Boolean {
        return when (id) {
            R.id.radio_veteran_y -> true
            R.id.radio_veteran_n -> false
            else -> false
        }
    }

    fun getViolence(id: Int): Boolean {
        return when (id) {
            R.id.radio_violence_y -> true
            R.id.radio_violence_n -> false
            else -> false
        }
    }

    fun getOffender(id: Int): Boolean {
        return when (id) {
            R.id.radio_offender_y -> true
            R.id.radio_offender_n -> false
            else -> false
        }
    }
}
