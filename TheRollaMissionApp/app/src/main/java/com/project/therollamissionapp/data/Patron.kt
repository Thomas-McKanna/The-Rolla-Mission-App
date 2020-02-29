package com.project.therollamissionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "patrons")
data class Patron (
    @ColumnInfo(name = "firstName") var firstName: String = "",
    @ColumnInfo(name = "lastName") var lastName: String = "",
    @PrimaryKey @ColumnInfo(name = "patronId") var id: String = UUID.randomUUID().toString()
)
