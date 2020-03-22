package com.project.therollamissionapp.data

import java.util.*

data class ExtendedPatron (
    var name: String = "",
    var birth_date: String = "",
    var gender: String = "",
    var phone: String = "",
    var veteran: Boolean = false,
    var violence: Boolean = false,
    var offender: Boolean = false,
    var time_homeless: String = "",
    var city: String = "",
    var reason: String = "",
    var id: String = UUID.randomUUID().toString()
)