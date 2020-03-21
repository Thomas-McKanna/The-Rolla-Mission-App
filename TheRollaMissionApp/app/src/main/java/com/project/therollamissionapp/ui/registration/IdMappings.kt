package com.project.therollamissionapp.ui.registration

import com.project.therollamissionapp.R

object IdMappings {
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
