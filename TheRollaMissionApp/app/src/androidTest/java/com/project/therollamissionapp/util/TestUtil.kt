package com.project.therollamissionapp.util

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron

class TestUtil {
    companion object {
        fun makePatron(): Patron {
            return Patron(
                name = listOf("Tim", "Eric", "David", "Steve").random()
            )
        }

        fun makeExtendedPatron(): ExtendedPatron {
            return ExtendedPatron(
                name = listOf("Tim", "Eric", "David", "Steve").random(),
                birth_date = "02/22/2020",
                gender = listOf("Male", "Female", "Other", "NA").random(),
                phone = "(555) 555-5555",
                city = listOf("Rolla", "St. Louis", "St. James").random(),
                reason = listOf("Family", "Work", "School", "Relationship", "Medical").random(),
                time_homeless = listOf("<Month", "1_3_months", "4_6_months", "6_12_months", "1_2_years", ">2years").random(),
                veteran = listOf(true, false).random(),
                violence = listOf(true, false).random(),
                offender = listOf(true, false).random()
            )
        }
    }
}


