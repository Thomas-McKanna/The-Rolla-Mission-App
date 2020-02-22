package com.project.therollamissionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "patrons")
data class Patron (
    @ColumnInfo(name = "firstName") var firstName: String = "",
    @ColumnInfo(name = "lastName") var lastName: String = "",
    @ColumnInfo(name = "dob") var dob: String = "",
    @ColumnInfo(name = "gender") var gender: String = "",
    @ColumnInfo(name = "phoneNumber") var phone: String = "",
    @ColumnInfo(name = "cityWhenBecameHomeless") var cityWhenBecameHomeless: String = "",
    @ColumnInfo(name = "ReasonForComingRolla") var reasonRolla: String = "",
    @ColumnInfo(name = "timeHomeless") var timeHomeless: String = "",
    @ColumnInfo(name = "veteran") var veteran: Boolean = false,
    @ColumnInfo(name = "fleeingViolence") var fleeingViolence: Boolean = false,
    @ColumnInfo(name = "sexOffender") var sexOffender: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "patronId") var id: String = UUID.randomUUID().toString()
)
