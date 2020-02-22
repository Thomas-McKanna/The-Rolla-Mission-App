package com.project.therollamissionapp.data

import androidx.room.Embedded
import androidx.room.Relation
import com.project.therollamissionapp.data.CheckIn
import com.project.therollamissionapp.data.Patron

data class PatronWithCheckIns (
    @Embedded val patron: Patron,
    @Relation(
        parentColumn = "patronId",
        entityColumn = "patronId"
    )
    val checkIns: List<CheckIn>
)