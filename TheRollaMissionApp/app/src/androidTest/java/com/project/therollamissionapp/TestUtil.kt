package com.project.therollamissionapp

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Patron

class TestUtil {
    companion object {
        fun makePatron(): Patron {
            return Patron(
                firstName = listOf("Tim", "Eric", "David", "Steve").random(),
                lastName = listOf("Heidecker", "Wierheim", "Lieb-Hart", "Brule").random()
            )
        }

        fun makeExtendedPatron(): ExtendedPatron {
            return ExtendedPatron(
                firstName = listOf("Tim", "Eric", "David", "Steve").random(),
                lastName = listOf("Heidecker", "Wierheim", "Lieb-Hart", "Brule").random(),
                dob = "02/22/2020",
                gender = listOf("Male", "Female", "Other", "NA").random(),
                phone = "(555) 555-5555",
                cityWhenBecameHomeless = listOf("Rolla", "St. Louis", "St. James").random(),
                reasonRolla = listOf("Family", "Work", "School", "Relationship", "Medical").random(),
                timeHomeless = listOf("<Month", "1_3_months", "4_6_months", "6_12_months", "1_2_years", ">2years").random(),
                veteran = listOf(true, false).random(),
                fleeingViolence = listOf(true, false).random(),
                sexOffender = listOf(true, false).random()
            )
        }
    }
}


