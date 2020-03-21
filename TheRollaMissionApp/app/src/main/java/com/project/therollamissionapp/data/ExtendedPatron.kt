package com.project.therollamissionapp.data

import java.util.*

data class ExtendedPatron (
    var name: String = "",
    var dob: String = "",
    var gender: String = "",
    var phone: String = "",
    var cityWhenBecameHomeless: String = "",
    var reasonRolla: String = "",
    var otherReason: String = "",
    var timeHomeless: String = "",
    var veteran: Boolean = false,
    var fleeingViolence: Boolean = false,
    var sexOffender: Boolean = false,
    var id: String = UUID.randomUUID().toString()
)