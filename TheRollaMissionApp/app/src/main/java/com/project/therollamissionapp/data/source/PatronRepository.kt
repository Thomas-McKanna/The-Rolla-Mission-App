package com.project.therollamissionapp.data.source

import com.project.therollamissionapp.data.ExtendedPatron
import com.project.therollamissionapp.data.Result
import com.project.therollamissionapp.data.Patron

interface PatronRepository {
    suspend fun insertPatron(extendedPatron: ExtendedPatron) : Result<Unit>

    suspend fun insertPatron(patron: Patron)

    suspend fun checkIn(patron: Patron): Result<Unit>

    suspend fun deletePatron(patron: Patron)
}